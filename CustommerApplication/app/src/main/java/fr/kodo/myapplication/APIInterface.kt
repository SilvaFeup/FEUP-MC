package fr.kodo.myapplication

import fr.kodo.myapplication.network.*
import retrofit2.http.Body
import retrofit2.http.POST

interface APIInterface {

    @POST("register")
    suspend fun register(@Body registerRequest: RegisterRequest): RegisterResponse

    @POST("login")
    suspend fun login(@Body loginRequest: LoginRequest): LoginResponse
}