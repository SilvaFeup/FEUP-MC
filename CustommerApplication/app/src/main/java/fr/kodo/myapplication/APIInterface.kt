package fr.kodo.myapplication

import fr.kodo.myapplication.model.Transaction
import fr.kodo.myapplication.network.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response


import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface APIInterface {

    @POST("register")
    suspend fun register(@Body registerRequest: RegisterRequest): RegisterResponse

    @POST("login")
    suspend fun login(@Body loginRequest: LoginRequest): LoginResponse

    @POST("voucher")
    suspend fun voucher(@Body voucherRequest: VoucherRequest): VoucherResponse

    @GET("/users/{uuid}/transactions")
    suspend fun getTransactionsByUserId(@Path("uuid") uuid: String): getPastTransactionResponse

}