package fr.kodo.myapplication.crypto

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties

import java.security.*
import java.security.spec.X509EncodedKeySpec
import java.util.*

class KeyStoreUtils {


    companion object {

        private const val KEY_SIZE = 512

        fun generateKeyPair(key_alias : String) {
            try {
                // Initialize KeyPairGenerator
                val kpg: KeyPairGenerator = KeyPairGenerator.getInstance(
                    KeyProperties.KEY_ALGORITHM_RSA, "AndroidKeyStore"
                )

                // Set key generation parameters
                val spec = KeyGenParameterSpec.Builder(
                    key_alias,
                    KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
                )
                    .setKeySize(KEY_SIZE)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_RSA_PKCS1)
                    .setDigests(KeyProperties.DIGEST_SHA256, KeyProperties.DIGEST_SHA512)
                    .setUserAuthenticationRequired(false)
                    .build()

                kpg.initialize(spec)

                // Generate key pair
                val keyPair: KeyPair = kpg.generateKeyPair()

                // Get public and private keys
                val publicKey: PublicKey = keyPair.public
                val privateKey: PrivateKey = keyPair.private



            } catch (e: NoSuchAlgorithmException) {
                e.printStackTrace()
            } catch (e: NoSuchProviderException) {
                e.printStackTrace()
            } catch (e: InvalidAlgorithmParameterException) {
                e.printStackTrace()
            }
        }

        fun getKeyPair(key_alias: String): KeyPair? {
            val keyStore = KeyStore.getInstance("AndroidKeyStore")
            keyStore.load(null)
            val privateKey = keyStore.getKey(key_alias, null) as? PrivateKey
            val publicKey = keyStore.getCertificate(key_alias)?.publicKey


            return if (privateKey != null && publicKey != null) {
                KeyPair(publicKey, privateKey)
            } else {
                null
            }
        }

        fun publicKeyToString(publicKey: PublicKey): String {
            val publicKeyBytes = publicKey.encoded
            val base64EncodedKey = Base64.getEncoder().encodeToString(publicKeyBytes)
            return base64EncodedKey
        }

        fun stringToPublicKey(base64EncodedKey: String): PublicKey {
            val publicKeyBytes = Base64.getDecoder().decode(base64EncodedKey)
            val keyFactory = KeyFactory.getInstance("RSA")
            val publicKeySpec = X509EncodedKeySpec(publicKeyBytes)
            val publicKey = keyFactory.generatePublic(publicKeySpec)
            return publicKey
        }




    }
}
