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
        const { name, email, username, password } = ctx.request.body;
    
        // Check if user already exists
        const userExists = await db.get('SELECT * FROM users WHERE email = ?', email);
        if (userExists) {
            ctx.status = 409;
            ctx.body = 'Email already registered';
            return;
        }

        // Encrypt password
        const saltRounds = 10;
        const hashedPassword = await bcrypt.hash(password, saltRounds);
    
        // Insert new user into the database
        const result = await db.run('INSERT INTO User (email,username, password, name, taxpayer_number, public_key,payment_card) VALUES (?, ?, ?, ?, to do,to do,11111)', [email, username, hashedPassword,name]);
        if (result.changes === 0) {
            throw new Error('Failed to register user');
        }
    
        ctx.status = 201;
        ctx.body = 'User registered successfully';
    } catch (err) {
      // Handle errors
    }
});


router.post('/login', async (ctx, next) => {
  try {
    const db = await init();
    const { email, password } = ctx.request.body;

    // Find user by email
    const user = await db.get('SELECT * FROM users WHERE email = ?', email);
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


