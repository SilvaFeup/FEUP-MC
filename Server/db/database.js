const path = require('path');

var db;

class Database {
    start() {
        const Database = import('sqlite-async');
        db = Database.open(path.resolve('db/database.sqlite'));
        console.log('Database started');
    }
}

module.exports = new Database();