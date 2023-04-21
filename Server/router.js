const {init} = require('./db/database');
const bcrypt = require('bcrypt');
const Router = require('koa-router');

const {v4: uuidv4} = require('uuid');
const { publicKey, privateKey } = require('./keys');

const router = new Router();

router.get('/', HomePage);

function HomePage(ctx) {
    ctx.body = 'Hello World';
}

router.post('/register', async (ctx,next) => {
  // code to add new user to database

  try {
    const db = await init();
    const { name, username, password, card_number, card_holder_name,expiration_month, expiration_year, cvv_code, public_key} = ctx.request.body;
  
        // Check if user already exists
    const userExists = await db.get('SELECT * FROM User WHERE username = ?', username);
    if (userExists) {
        ctx.body = { error: 1, message: 'User already exists'};
        return;
    }

    var card = 0;

    // Check if card already exists
    const cardExists = await db.get('SELECT * FROM PaymentCard WHERE card_number = ?', card_number);
    if (cardExists) {
      card = cardExists.id;
    }
    else{
      //Insert new card into the database

      const result = await db.run('INSERT INTO PaymentCard (card_number,card_holder_name, expiration_month,expiration_year, cvv_code) VALUES (?, ?, ?, ?,?)', [card_number,card_holder_name, expiration_month,expiration_year, cvv_code]);
      if (result.changes === 0) {
          throw new Error('Failed to register card');
      }

      const cardObject = await db.get('SELECT id FROM PaymentCard WHERE card_number = ?', card_number);
      card = cardObject.id;
    }

    const unique_id = uuidv4(); 

    // Encrypt password
    const saltRounds = 10;
    const hashedPassword = await bcrypt.hash(password, saltRounds);
  
    // Insert new user into the database
    const result_u = await db.run('INSERT INTO User (uuid, username, password, name, public_key,payment_card) VALUES (?, ?, ?, ?,? ,?)', [unique_id, username, hashedPassword,name,public_key,card]);
    if (result_u.changes === 0) {
        throw new Error('Failed to register user');
    }
  
    // Return a success response
    ctx.body = { error: 0, message: 'User registered successfully'};
    
  } catch (err) {
    // Handle errors
  }

});


router.get('/users', async (ctx, next) => {
    try {
      const db = await init();
      const rows = await db.all('SELECT * FROM User');
      ctx.body = rows;
    } catch (err) {
      if (err.message.includes('no such table')) {
        console.error(err.message);
        ctx.status = 500;
        ctx.body = 'Error retrieving users: table does not exist';
      } else {
        throw err;
      }
    }
});


router.post('/login', async (ctx, next) => {
  try {
    const db = await init();
    const { username, password } = ctx.request.body;

    // Find user by username
    const user = await db.get('SELECT * FROM User WHERE username = ?', username);
    if (!user) {
      ctx.body = { error: 1, message: 'Username or password is incorrect'};
      return;
    }

    // Compare password
    const passwordMatch = await bcrypt.compare(password, user.password);
    if (!passwordMatch) {
      ctx.body = { error: 1, message: 'Username or password is incorrect'};
      return;
    }

    ctx.status = 200;
    ctx.body = { message: 'User logged in successfully', userId: user.uuid, supermarket_publickey: publicKey };
    return;
  } catch (err) {
    // Handle errors
    console.log(err.stack)
  }
});


router.post('/checkout', async(ctx,next) => {
  var somethingIsWrong = false;
  var total = 0;
  var discount = 0;

  try{
    const db = await init();
    const { idProductList, productQuantityList, userId, useAccumulatedDiscount, voucherId, date } = ctx.request.body;

    //find user
    var user = await db.get("SELECT * FROM User WHERE uuid = ?",userId)
    
    if(!user){
      somethingIsWrong=true
      ctx.body = {message: 'user not find'}
    }
    else{discount = user.accumulated_discount}
    

    //find all products
    if(!somethingIsWrong){
      var dataBaseProducts = [];  //List of all products in the data base
      dataBaseProducts = await db.all('SELECT * FROM Product' );
    }
    

      //check if all products in the basket exist in the data base
    if(!somethingIsWrong){
      var productsInBasket = [];  //List that will be completed with the database products that are in the basket
      for (let i = 0; i < idProductList.length; i++) {
        var productFound = null;

        dataBaseProducts.forEach( DBproduct=> {
          if(DBproduct.uuid == idProductList[i]){ 
            productFound = DBproduct;
          }
        })

        if(!productFound){
          ctx.body = {message: 'One of the products does not exist in our supermarket'};
          somethingIsWrong=true;
        }
        productsInBasket.push(productFound);
      }
    }

    //Check the voucher id
    var voucherIdForDb =0
        if(voucherId>0){
          //is the id of voucher exist?
          var listVoucher = await db.all("SELECT * FROM Voucher Where owner = ?",user.id)
          var present = false;
          listVoucher.forEach(voucher => {
            if(voucher.id == voucherId){present = true}
          })
          voucherIdForDb =voucherId
          if(!present){
            ctx.body = {message: 'Voucher unknown'}
            somethingIsWrong = true;
          }
        }
        


      //Total calculation
      if(!somethingIsWrong){
        if(idProductList.length == productQuantityList.length){
          for(let i=0; i<idProductList.length; i++){
            total = total + parseFloat(productsInBasket[i].price_tag*productQuantityList[i])
          }
          total = parseFloat(total.toFixed(2))
        }
        else{
          ctx.body = {message: 'not the same number of products and quantity'}
          somethingIsWrong=true;
        } 
      }


      //use Discount accumulated
      if(!somethingIsWrong){
        if(useAccumulatedDiscount==1){

          if(total >= discount){
            total = total - discount 
            await db.run("UPDATE User SET accumulated_discount = '0' WHERE uuid = ?",user.uuid)
            discount = 0
          }
          else{
            var difference = total
            total = 0
            discount = discount - difference
            await db.run("UPDATE User SET accumulated_discount = ? WHERE uuid = ?",discount,user.uuid)
            
          }
        }
      }

      //use a voucher
      if(!somethingIsWrong){

          //calcul of value and add it to the db
          if(present == true){
            var voucherValue = total*15/100;
            //console.log(voucherValue)
            //console.log(voucherValue.toFixed(2))
            //console.log(discount)
            
            discount = discount + voucherValue
            discount = parseFloat(discount.toFixed(2))
            //console.log(discount)
            
            await db.run("UPDATE User set accumulated_discount = ? WHERE uuid = ?",discount, user.uuid)
            await db.run("DELETE FROM Voucher WHERE id = ?",voucherId)
          }
        }
        else{voucherIdForDb = -1}

      //Creation of voucher
      if(!somethingIsWrong){
        var totalSinceNextVoucher = parseFloat(user.total_paid_since_last_voucher + total)
        var numberOfVoucher = totalSinceNextVoucher/100.0
        numberOfVoucher =parseFloat(Math.round(numberOfVoucher))

        for(i=0; i<numberOfVoucher; i++){
          var voucherUUID = uuidv4()
          await db.run("INSERT INTO Voucher(uuid, owner) VALUES(?,?)",voucherUUID,user.id)
        }
        totalSinceNextVoucher = totalSinceNextVoucher - (numberOfVoucher*100)
        await db.run("UPDATE User SET total_paid_since_last_voucher = ? WHERE uuid = ?",totalSinceNextVoucher.toFixed(2), user.uuid)        
      }

      //send a validation to the application
      if(!somethingIsWrong){
        //await db.run("SELECT accumulated_discount FROM User WHERE uuid = ?",user.uuid)
        await db.run("INSERT INTO OrderInfo (total_amount, customer_id, voucher_id, date_order) Values(?,?,?,?)",total,user.id,voucherIdForDb,date)
        var orderId = await db.get("SELECT last_insert_rowid() AS id")

        for(i=0; i<productsInBasket.length;i++){
          await db.run("INSERT INTO OrderItem(order_id, final_quantity, product) VALUES(?,?,?)",orderId.id,productQuantityList[i],productsInBasket[i].id)
        }

        ctx.status = 200;
        ctx.body = {message: 'Checkout valid',total: total,discount: discount};
      }

  }catch(err){
    console.log(err.stack)
  }
});



router.post('/voucher', async (ctx, next) => {
  try{
    const db = await init();
    const {owner} = ctx.request.body;

    const user = await db.get('SELECT * FROM User WHERE uuid = ?', owner);

    const voucherList = await db.all('SELECT * FROM Voucher WHERE owner = ?', user.id);
    //console.log(voucherList);

    ctx.body = {voucherList};
    
    
  }catch(err){
    //Handle errors
    console.error(err.stack)
  }
});

// GET transactions by uuid
router.get('/users/:uuid/transactions', async (ctx) => {
  const uuid = ctx.params.uuid;
  const db = await init();
  try {

    //need to ger the user id from the uuid that comes with the path
    const userId = await db.get('SELECT id FROM User WHERE uuid = ?', [uuid]);

    // retrieve transactions from the database using the user ID
    const rows = await db.all('SELECT * FROM OrderInfo WHERE customer_id = ?', userId.id);

    // return the transactions as JSON
    //console.log(rows);
    ctx.body = {pastTransactionList: rows};

  } catch (err) {

    console.error(err.stack);
    // handle errors appropriately
    ctx.status = 500;
    ctx.body = { error: 'Failed to retrieve transactions' };

  }
});

router.post('/verifySignature', async (ctx,next) => {
  try{
    const db = await init();
    const {uuid} = ctx.request.body
  
    var userKey = await db.get("SELECT * FROM User WHERE uuid =?",uuid)

    if (!userKey){
      ctx.body = {error: "Failed to find key"}
    }
    else{
      ctx.status = 200
      ctx.body = {message: "Response find",pubKey: userKey.public_key}
    }
  }
  catch (err) {
    console.error(err.stack)
    ctx.status = 500
    ctx.body = { error: 'Failed to find key'}
  }

});

module.exports = router;
