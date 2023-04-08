package fr.kodo.myapplication.controller


import android.util.Log
import fr.kodo.myapplication.APIInterface
import fr.kodo.myapplication.network.LoginRequest
import fr.kodo.myapplication.network.RegisterRequest
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.PasswordAuthentication


class AuthController {

    val apiInterface: APIInterface by lazy {
        Retrofit.Builder()
            .baseUrl("http://10.0.2.2:3000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(APIInterface::class.java)
    }

    suspend fun register(
        name: String,
        userName: String,
        passwordAuthentication: String,
        confirmPasswordAuthentication: String,
        card_number: String,
        card_holder_name: String,
        expiration_month: Int,
        expiration_year: Int,
        security_code: String,
        public_key: String,
    ): Int {
        //if one of the fields is empty, return
        if (name.isEmpty() || userName.isEmpty() || passwordAuthentication.isEmpty() || confirmPasswordAuthentication.isEmpty() || card_number.isEmpty() || card_holder_name.isEmpty() || security_code.isEmpty()) {
            throw Exception("All fields must be filled")
        }
        if (!passwordAuthentication.contentEquals(confirmPasswordAuthentication)) {
            throw Exception("Passwords do not match")
        }




        val registerRequest = RegisterRequest(userName,passwordAuthentication,name,card_number,card_holder_name, expiration_month,expiration_year, security_code,public_key);

        val registerInfo = apiInterface.register(registerRequest)

        Log.e("Register Info: ", registerInfo.message);

        //Register Info has (message, uuid, supermarket public key)


        if(registerInfo.message.contentEquals("User registered successfully")){
            return 1;
        }


        return -1;

    }


    suspend fun login(

        userName: String,
        passwordAuthentication: String,

    ) :  Int{
        //if one of the fields is empty, return
        if (userName.isEmpty() || passwordAuthentication.isEmpty()) {
            throw Exception("All fields must be filled")
        }


        val loginRequest = LoginRequest(userName,passwordAuthentication)
        val loginInfo = apiInterface.login(loginRequest)

        Log.e("Login Info: ", loginInfo.message);

        //login Info has (message)

        if(loginInfo.message.contentEquals("User logged in successfully")){
            return 1;
        }


        return -1;
    }
}