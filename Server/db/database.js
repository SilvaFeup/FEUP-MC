const path = require('path');

var db;

(async () => {
    const { Database } = await import('sqlite-async');
    db = await Database.open(path.resolve('db/users.db'));
    console.log('connected to users.db');
  })();

class Database {
    
}

module.exports = new Database();