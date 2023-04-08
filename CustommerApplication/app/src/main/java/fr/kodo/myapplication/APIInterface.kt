package fr.kodo.myapplication

import fr.kodo.myapplication.network.LoginRequest
import fr.kodo.myapplication.network.LoginResponse
import fr.kodo.myapplication.network.RegisterRequest
import fr.kodo.myapplication.network.RegisterResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface APIInterface {

    @POST("register")
    suspend fun register(@Body registerRequest: RegisterRequest): RegisterResponse

    @POST("login")
    suspend fun login(@Body loginRequest: LoginRequest): LoginResponse
}