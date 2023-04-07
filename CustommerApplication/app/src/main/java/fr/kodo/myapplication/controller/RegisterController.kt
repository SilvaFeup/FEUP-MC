package fr.kodo.myapplication.controller

import android.util.Log
import fr.kodo.myapplication.APIInterface
import fr.kodo.myapplication.network.RegisterRequest
import fr.kodo.myapplication.network.RegisterResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.PasswordAuthentication


class RegisterController {

    val apiInterface: APIInterface by lazy {
        Retrofit.Builder()
            .baseUrl("http://10.0.2.2:3000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(APIInterface::class.java)
    }

    suspend fun register(
        name: String,
        passwordAuthentication: PasswordAuthentication,
        confirmPasswordAuthentication: PasswordAuthentication,
        card_number: String,
        card_holder_name: String,
        expiration_month: Int,
        expiration_year: Int,
        security_code: String
    ): Int {
        //if one of the fields is empty, return
        if (name.isEmpty() || passwordAuthentication.userName.isEmpty() || passwordAuthentication.password.isEmpty() || confirmPasswordAuthentication.password.isEmpty() || card_number.isEmpty() || card_holder_name.isEmpty() || security_code.isEmpty()) {
            throw Exception("All fields must be filled")
        }
        if (!passwordAuthentication.password.contentEquals(confirmPasswordAuthentication.password)) {
            throw Exception("Passwords do not match")
        }

        val registerRequest = RegisterRequest(passwordAuthentication.userName,passwordAuthentication.password.toString(),name,card_number,card_holder_name, expiration_month,expiration_year, security_code,"");

        val registerInfo = apiInterface.register(registerRequest)

        Log.e("Register Info: ", registerInfo.message);

        //Register Info has (message, uuid, supermarket public key)


        return 0;
    }
}