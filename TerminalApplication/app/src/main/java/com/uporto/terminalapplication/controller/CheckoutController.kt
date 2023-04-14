package com.uporto.terminalapplication.controller

import android.util.Log
import android.widget.Toast
import com.uporto.terminalapplication.APIInterface
import com.uporto.terminalapplication.model.Product
import com.uporto.terminalapplication.network.CheckoutRequest
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.LocalDate
import java.util.UUID

class CheckoutController {
    val apiInterface: APIInterface by lazy {
        Retrofit.Builder()
            //.baseUrl("http://192.168.1.81:3000/")//Axel
            //.baseUrl("http://192.168.1.80:3000/")//aurélien
            .baseUrl("http://192.168.191.163:3000/")//aurélien with his own connexion
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
    ): ArrayList<Float> {
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

        var result = ArrayList<Float>()
        if (checkoutInfo.message.contentEquals("Checkout valid")) {
            result.add(checkoutInfo.total)
            result.add(checkoutInfo.discount)
        }
        else result.add(-1f)

        return result
    }
}