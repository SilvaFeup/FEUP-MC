const crypto = require('crypto');

// Generate a new RSA key pair with 512-bit key length
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

module.exports = {
  publicKey,
  privateKey,
};
