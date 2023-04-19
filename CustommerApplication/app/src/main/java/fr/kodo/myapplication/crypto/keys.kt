package fr.kodo.myapplication.crypto

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Log
import fr.kodo.myapplication.model.ANDROID_KEY_STORE
import fr.kodo.myapplication.model.KEY_SIZE

import java.security.*
import java.security.spec.X509EncodedKeySpec
import java.util.*


class KeyStoreUtils {
    companion object {

        fun generateKeyPair(key_alias : String) {
            try {
                // Initialize for sha256withRSA algorithm
                val kpg = KeyPairGenerator.getInstance(
                    KeyProperties.KEY_ALGORITHM_RSA,
                    ANDROID_KEY_STORE
                )

                val spec = KeyGenParameterSpec.Builder(
                    key_alias,
                    KeyProperties.PURPOSE_SIGN or KeyProperties.PURPOSE_VERIFY
                )
                    .setDigests(KeyProperties.DIGEST_SHA256)
                    .setSignaturePaddings(KeyProperties.SIGNATURE_PADDING_RSA_PKCS1)
                    .setKeySize(KEY_SIZE)
                    .build()

                kpg.initialize(spec)
                kpg.generateKeyPair()

            } catch (e: NoSuchAlgorithmException) {
                e.printStackTrace()
            } catch (e: NoSuchProviderException) {
                e.printStackTrace()
            } catch (e: InvalidAlgorithmParameterException) {
                e.printStackTrace()
            }
        }

        fun getKeyPair(key_alias: String): KeyPair? {
            val keyStore = KeyStore.getInstance(ANDROID_KEY_STORE)
            keyStore.load(null)
            val privateKey = keyStore.getKey(key_alias, null) as? PrivateKey
            val publicKey = keyStore.getCertificate(key_alias)?.publicKey

            return if (privateKey != null && publicKey != null) {
                KeyPair(publicKey, privateKey)
            } else {
                null
            }
        }

        fun getPublicKeyFromString(supermarketPublicKey: String): PublicKey {
            var cleanSupermarketPublicKey =
                supermarketPublicKey.replace("-----BEGIN PUBLIC KEY-----\n", "")
            cleanSupermarketPublicKey =
                cleanSupermarketPublicKey.replace("-----END PUBLIC KEY-----", "")
            cleanSupermarketPublicKey = cleanSupermarketPublicKey.replace("\n", "")
            val keyStore = KeyStore.getInstance(ANDROID_KEY_STORE)
            keyStore.load(null)
            val encoded = Base64.getDecoder().decode(cleanSupermarketPublicKey)
            val keySpec = X509EncodedKeySpec(encoded)
            val keyFactory = KeyFactory.getInstance("RSA")
            return keyFactory.generatePublic(keySpec)
        }
    }
}
