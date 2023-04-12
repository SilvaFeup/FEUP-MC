package com.uporto.terminalapplication.controller

import android.util.Log
import com.uporto.terminalapplication.APIInterface
import com.uporto.terminalapplication.model.Product
import com.uporto.terminalapplication.network.CheckoutRequest
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.UUID

class CheckoutController {
    val apiInterface: APIInterface by lazy {
        Retrofit.Builder()
            //.baseUrl("http://192.168.1.81:3000/")//Axel
            //.baseUrl("http://192.168.1.80:3000/")//aurélien
            .baseUrl("http://192.168.250.163:3000/")//aurélien with his own connexion
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(APIInterface::class.java)
    }

    suspend fun checkout(
        idProductList: ArrayList<UUID>,
        productQuantityList: ArrayList<Int>,
        userId: UUID,
        useAccumulatedDiscount: Float,
        voucherId: Int
    ): Int {
        //if one of the fields is empty, return
        if (idProductList.isEmpty() || productQuantityList.isEmpty()) {
            throw Exception("All fields must be filled")
        }

        val checkoutRequest = CheckoutRequest(
            idProductList,
            productQuantityList,
            userId,
            useAccumulatedDiscount,
            voucherId
        )
        val checkoutInfo = apiInterface.checkout(checkoutRequest)
        Log.e("Checkout info: ", checkoutInfo.message)

        if (checkoutInfo.message.contentEquals("Checkout valid")) {
            return 1
        }
        return 0
    }
}