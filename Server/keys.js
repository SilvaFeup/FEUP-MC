const crypto = require('crypto');

//read the keys from a file (if they exist) or generate a new pair
const fs = require('fs');
let publicKey, privateKey;
if (fs.existsSync('public.pem') && fs.existsSync('private.pem')) {
  console.log('Loading keys from file');
  publicKey = fs.readFileSync('public.pem', 'utf8');
  privateKey = fs.readFileSync('private.pem', 'utf8');
} else {
  console.log('Generating new keys, restart the server');
  const { publicKey, privateKey } = crypto.generateKeyPairSync('rsa', {
    modulusLength: 512,
    publicKeyEncoding: {
      type: 'spki',
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

//console.log('publicKey: ', publicKey);
//console.log('publicKey base64: ', Buffer.from(publicKey).toString('base64'));



//encrypt with RSA this messages with the private key : "625a0656-d7b6-11ed-afa1-0242ac120002,product0,3.99"
product0 = "625a0656-d7b6-11ed-afa1-0242ac120002,product0,3.99"
product1 = "77f2fe28-d7b6-11ed-afa1-0242ac120002,product1,100.0"
product2 = "807e8a08-d7b6-11ed-afa1-0242ac120002,product2,0.95"

const encrypted = crypto.privateEncrypt(privateKey,
  Buffer.from(product0, 'utf8')
);
//console.log('encrypted: ', encrypted.toString('base64'));
const encrypted2 = crypto.privateEncrypt(privateKey,
  Buffer.from(product1, 'utf8')
);
//console.log('encrypted: ', encrypted2.toString('base64'));
const encrypted3 = crypto.privateEncrypt(privateKey,
  Buffer.from(product2, 'utf8')
);
//console.log('encrypted: ', encrypted3.toString('base64'));


//const decrypted = crypto.publicDecrypt(publicKey, encrypted);
//console.log('decrypted: ', decrypted.toString());


module.exports = {
  publicKey,
  privateKey
};
