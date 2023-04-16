package com.uporto.terminalapplication.controller

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.uporto.terminalapplication.APIInterface
import com.uporto.terminalapplication.network.CheckoutRequest
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.LocalDate
import java.util.UUID

class CheckoutController() {
    val apiInterface: APIInterface by lazy {
        Retrofit.Builder()
            .baseUrl("http://192.168.1.81:3000/")//Axel
            //.baseUrl("http://192.168.1.80:3000/")//aurélien
            //.baseUrl("http://192.168.248.163:3000/")//aurélien with his own connexion
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
        Log.e("date",date.toString())
        val checkoutInfo = apiInterface.checkout(checkoutRequest)
        Log.e("Checkout info: ", checkoutInfo.message)

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
}