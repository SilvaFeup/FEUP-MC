package com.uporto.terminalapplication

import com.uporto.terminalapplication.network.CheckoutRequest
import com.uporto.terminalapplication.network.CheckoutResponse
import com.uporto.terminalapplication.network.KeysRequest
import com.uporto.terminalapplication.network.KeysResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface APIInterface {
    @POST("checkout")
    suspend fun checkout(@Body checkoutRequest: CheckoutRequest): CheckoutResponse

    @POST("verifySignature")
    suspend fun verifySignature(@Body keysRequest: KeysRequest): KeysResponse
}