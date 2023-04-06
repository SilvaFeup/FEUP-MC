package fr.kodo.myapplication.controller

import android.util.Log
import android.widget.Toast
import fr.kodo.myapplication.APIInterface
import fr.kodo.myapplication.model.PaymentCard
import fr.kodo.myapplication.model.User
import retrofit2.Retrofit
import retrofit2.awaitResponse
import retrofit2.converter.gson.GsonConverterFactory
import java.net.PasswordAuthentication
import java.util.*

class RegisterController {

    val apiInterface: APIInterface by lazy {
        Retrofit.Builder()
            .baseUrl("http://localhost:3000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(APIInterface::class.java)
    }

    fun register(
        name: String,
        passwordAuthentication: PasswordAuthentication,
        confirmPasswordAuthentication: PasswordAuthentication,
        card_number: String,
        card_holder_name: String,
        expiration_date: String,
        security_code: String
    ): Int {
        //if one of the fields is empty, return
        if (name.isEmpty() || passwordAuthentication.userName.isEmpty() || passwordAuthentication.password.isEmpty() || confirmPasswordAuthentication.password.isEmpty() || card_number.isEmpty() || card_holder_name.isEmpty() || expiration_date.isEmpty() || security_code.isEmpty()) {
            //throw Exception("All fields must be filled")
        }
        if (!passwordAuthentication.password.contentEquals(confirmPasswordAuthentication.password)) {
            throw Exception("Passwords do not match")
        }
        val user = User(UUID.randomUUID(), passwordAuthentication.userName, passwordAuthentication.password.toString(), name, "", PaymentCard(0, card_number, card_holder_name, 0, 0, security_code))

        val response = apiInterface.register(user).execute()
        if (!response.isSuccessful) {
            throw Exception("Error while registering, reponse not successful")
        }
        return response.code()
    }
}