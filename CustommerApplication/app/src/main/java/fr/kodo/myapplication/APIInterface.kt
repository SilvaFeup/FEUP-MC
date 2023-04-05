package fr.kodo.myapplication

import fr.kodo.myapplication.model.User
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface APIInterface {

    @POST("register")
    fun register(@Body user: User): Call<User>
}