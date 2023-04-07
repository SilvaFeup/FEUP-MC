const path = require('path');
const fs = require('fs');


const init = async () => {
  const { Database } = await import('sqlite-async');

  const db = await Database.open(path.resolve('./db/database.db'));
  // Read SQL file
  //const sql = fs.readFileSync(path.resolve('./db/tables.sql'), 'utf-8');

  // Run SQL commands
  //await db.exec(sql);

  return db;
};




module.exports = {
  init
};