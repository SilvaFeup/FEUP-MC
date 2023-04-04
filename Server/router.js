const {init} = require('./db/database');
const bcrypt = require('bcrypt');
const Router = require('koa-router');

const router = new Router();

router.get('/', HomePage);

function HomePage(ctx) {
    ctx.body = 'Hello World';
}


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


router.post('/register', async (ctx, next) => {
    try {
        const db = await init();
        const { name, email, username, password, card_number, card_holder_name,expiration_month, expiration_year, cvv_code, public_key} = ctx.request.body;
    
        // Check if user already exists
        const userExists = await db.get('SELECT * FROM User WHERE email = ?', email);
        if (userExists) {
            ctx.status = 409;
            ctx.body = 'Email already registered';
            return;
        }

        const result_paymentCard = await insertPaymentCardIntoDB(card_number,card_holder_name,expiration_month,expiration_year,cvv_code);

        if(result_paymentCard == -1){
          ctx.status = 409;
          ctx.body = 'Payment card already registered';
          return;
        }

        const card = await db.get('SELECT id FROM PaymentCard WHERE card_number = ?', card_number);

        // Encrypt password
        const saltRounds = 10;
        const hashedPassword = await bcrypt.hash(password, saltRounds);
    
        // Insert new user into the database
        const result = await db.run('INSERT INTO User (email,username, password, name, public_key,payment_card) VALUES (?, ?, ?, ?,? ,?)', [email, username, hashedPassword,name,public_key,card]);
        if (result.changes === 0) {
            throw new Error('Failed to register user');
        }
    
        ctx.status = 201;
        ctx.body = 'User registered successfully';
    } catch (err) {
      // Handle errors
    }
});

async function insertPaymentCardIntoDB(card_number,card_holder_name,expiration_month,expiration_year,cvv_code){

      // Check if card already exists
      const cardExists = await db.get('SELECT * FROM PaymentCard WHERE card_number = ?', card_number);
      if (cardExists) {
        return -1;
      }

      //Insert new card into the database

      const result = await db.run('INSERT INTO PaymentCard (card_number,card_holder_name, expiration_month,expiration_year, cvv_code) VALUES (?, ?, ?, ?,?)', [card_number,card_holder_name, expiration_month,expiration_year, cvv_code]);
      if (result.changes === 0) {
          throw new Error('Failed to register card');
      }

      return 0;
}


router.post('/login', async (ctx, next) => {
  try {
    const db = await init();
    const { email, password } = ctx.request.body;

    // Find user by email
    const user = await db.get('SELECT * FROM User WHERE email = ?', email);
    if (!user) {
      ctx.status = 401;
      ctx.body = 'Email or password is incorrect';
      return;
    }

    // Compare password
    const passwordMatch = await bcrypt.compare(password, user.password);
    if (!passwordMatch) {
      ctx.status = 401;
      ctx.body = 'Email or password is incorrect';
      return;
    }

    ctx.status = 200;
    ctx.body = 'Login successful';
  } catch (err) {
    // Handle errors
  }
});

module.exports = router;


