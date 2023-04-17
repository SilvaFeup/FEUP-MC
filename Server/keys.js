const crypto = require('crypto');

//read the keys from a file (if they exist) or generate a new pair
const fs = require('fs');
let publicKey, privateKey;
if (fs.existsSync('public.pem') && fs.existsSync('private.pem')) {
  console.log('Loading keys from file');
  publicKey = fs.readFileSync('public.pem', 'utf8');
  privateKey = fs.readFileSync('private.pem', 'utf8');
} else {
  console.log('Generating new keys');
  const { publicKey, privateKey } = crypto.generateKeyPairSync('rsa', {
    modulusLength: 512,
    publicKeyEncoding: {
      type: 'pkcs1',
      format: 'pem',
    },
    privateKeyEncoding: {
      type: 'pkcs1',
      format: 'pem',
    },
  });
  fs.writeFileSync('public.pem', publicKey);
  fs.writeFileSync('private.pem', privateKey);
}

module.exports = {
  publicKey,
  privateKey,
};
