const koa = require('koa');

const app = new koa();
const router = require('./router');

app.use(router.routes());
app.listen(3000, () => console.log('Server started on port 3000'));