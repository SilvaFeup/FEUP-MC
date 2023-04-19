package com.uporto.terminalapplication.controller

import android.util.Log
import com.uporto.terminalapplication.APIInterface
import com.uporto.terminalapplication.network.CheckoutRequest
import com.uporto.terminalapplication.network.KeysRequest
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.security.KeyFactory
import java.security.KeyStore
import java.security.PublicKey
import java.security.Signature
import java.security.interfaces.RSAPublicKey
import java.security.spec.X509EncodedKeySpec
import java.time.LocalDate
import java.util.*
import kotlin.collections.ArrayList

class CheckoutController() {
    val apiInterface: APIInterface by lazy {
        Retrofit.Builder()
            //.baseUrl("http://192.168.1.81:3000/")//Axel
            //.baseUrl("http://192.168.1.80:3000/")//aurélien
            .baseUrl("http://192.168.83.163:3000/")//aurélien with his own connexion
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(APIInterface::class.java)
    }

    suspend fun verifySignature(uuid: UUID, userSignature : String,message : String): Boolean{
        val keysRequest = KeysRequest(uuid)
        val response = apiInterface.verifySignature(keysRequest)
        var verified = false

        if (response.message.contentEquals("Response find")){
            val pubKey = getPublicKeyFromString(response.pubKey)

            verified = Signature.getInstance("SHA256WithRSA").run {
                initVerify(pubKey)
                update(message.toByteArray())
                verify(userSignature.toByteArray())
            }
        }
        return verified
    }

    suspend fun checkout(
        idProductList: ArrayList<UUID>,
        productQuantityList: ArrayList<Int>,
        userId: UUID,
        useAccumulatedDiscount: Float,
        voucherId: Int
    ): ArrayList<String> {
        //if one of the fields is empty, return
        if (idProductList.isEmpty() || productQuantityList.isEmpty()) {
            throw Exception("All fields must be filled")
        }

        var date = LocalDate.now()
        val checkoutRequest = CheckoutRequest(
            idProductList,
            productQuantityList,
            userId,
            useAccumulatedDiscount,
            voucherId,
            date.toString()
        )
        val checkoutInfo = apiInterface.checkout(checkoutRequest)

        var result = ArrayList<String>()
        if (checkoutInfo.message.contentEquals("Checkout valid")) {
            result.add(checkoutInfo.total.toString())
            result.add(checkoutInfo.discount.toString())
            result.add(checkoutInfo.message)
        }
        else {
            result.add("-1")
            result.add(checkoutInfo.message)
        }

        return result
    }

    fun getPublicKeyFromString(publicKey: String): PublicKey {
        var cleanPublicKey =
            publicKey.replace("-----BEGIN PUBLIC KEY-----\n", "")
        cleanPublicKey =
            cleanPublicKey.replace("-----END PUBLIC KEY-----", "")
        cleanPublicKey = cleanPublicKey.replace("\n", "")
        val keyStore = KeyStore.getInstance("AndroidKeyStore")
        keyStore.load(null)
        val encoded = Base64.getDecoder().decode(cleanPublicKey)
        val keySpec = X509EncodedKeySpec(encoded)
        val keyFactory = KeyFactory.getInstance("RSA")
        return keyFactory.generatePublic(keySpec)
    }
}