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
        ctx.status = 409;
        ctx.body = { message: 'User already exists'};
        return;
    }

          // Check if card already exists
      const cardExists = await db.get('SELECT * FROM PaymentCard WHERE card_number = ?', card_number);
      if (cardExists) {
        return;
      }

      //Insert new card into the database

      const result = await db.run('INSERT INTO PaymentCard (card_number,card_holder_name, expiration_month,expiration_year, cvv_code) VALUES (?, ?, ?, ?,?)', [card_number,card_holder_name, expiration_month,expiration_year, cvv_code]);
      if (result.changes === 0) {
          throw new Error('Failed to register card');
      }

      const card = await db.get('SELECT id FROM PaymentCard WHERE card_number = ?', card_number);

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
      ctx.status = 200;
      ctx.body = { message: 'User registered successfully'};

    
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
      ctx.status = 401;
      ctx.body = { message: 'username or password is incorrect'};
      return;
    }

    // Compare password
    const passwordMatch = await bcrypt.compare(password, user.password);
    if (!passwordMatch) {
      ctx.status = 401;
      ctx.body = { message: 'username or password is incorrect'};
      return;
    }

    ctx.status = 200;
    ctx.body = { message: 'User logged in successfully', userId: user.uuid, supermarket_publickey: publicKey };
    console.log(user.uuid);
  } catch (err) {
    // Handle errors
  }
});


router.post('/checkout', async(ctx,next) => {
  var missingProduct = false;
  try{
    const db = await init();
    const { idProductList, productQuantityList, userId, useAccumulatedDiscount, voucherId } = ctx.request.body;

    //find all products
    var dataBaseProducts = [];  //List of all products in the data base
    dataBaseProducts = await db.all('SELECT * FROM Product' );

    console.log(dataBaseProducts)
    var productsInBasket = [];  //List that will be completed with the database products that are in the basket

      //check if all products in the basket exist in the data base
      for (let i = 0; i < idProductList.length; i++) {
        var productFound = null;

        dataBaseProducts.forEach( DBproduct=> {
          if(DBproduct.uuid == idProductList[i]){ 
            productFound = DBproduct;
          }
        })

        if(!productFound){
          
          ctx.body = {message: 'One of the products does not exist in our supermarket'};
          missingProduct=true;
        }
        productsInBasket.push(productFound);
      }

      

    if(!missingProduct){
      ctx.status = 200;
      ctx.body = {message: 'Checkout valid'};
    }

  }catch(err){
    //Handle errors
    console.log(err.stack)
  }
});

module.exports = router;


