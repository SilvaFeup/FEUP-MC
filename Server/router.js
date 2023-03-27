const DB = require('./db/database');
const Router = require('koa-router');

const router = new Router();

router.get('/', HomePage);

function HomePage(ctx) {
    ctx.body = 'Hello World';
}

module.exports = router;
