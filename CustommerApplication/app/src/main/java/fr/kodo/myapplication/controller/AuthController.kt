package fr.kodo.myapplication.controller


import android.content.Context
import android.util.Base64
import fr.kodo.myapplication.APIInterface
import fr.kodo.myapplication.model.Session
import fr.kodo.myapplication.model.Voucher
import fr.kodo.myapplication.model.Transaction
import fr.kodo.myapplication.network.LoginRequest
import fr.kodo.myapplication.network.RegisterRequest
import fr.kodo.myapplication.network.VoucherRequest
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.UUID

class AuthController {


    private val apiInterface: APIInterface by lazy {
        Retrofit.Builder()
            //.baseUrl("http://192.168.1.81:3000/")//Axel
            //.baseUrl("http://10.0.2.2:3000/")//emulator
            //.baseUrl("http://192.168.1.80:3000/")//aurélien
            //.baseUrl("http://192.168.250.163:3000/")//aurélien with his own connexion
            .baseUrl("http://192.168.225.102:3000/")//Axel with his own connexion
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
        expiration_month: String,
        expiration_year: String,
        security_code: String
    ) {
        //if one of the fields is empty, return
        if (name.isEmpty() || userName.isEmpty() || passwordAuthentication.isEmpty() || confirmPasswordAuthentication.isEmpty() || card_number.isEmpty() || card_holder_name.isEmpty() || security_code.isEmpty()) {
            throw Exception("All fields must be filled")
        }
        if (!passwordAuthentication.contentEquals(confirmPasswordAuthentication)) {
            throw Exception("Passwords do not match")
        }

        KeyStoreUtils.generateKeyPair(userName)

        val publicKey = KeyStoreUtils.getKeyPair(userName)

        val registerRequest = RegisterRequest(userName,passwordAuthentication,name,card_number,card_holder_name, expiration_month.toInt(),expiration_year.toInt(), security_code, Base64.encodeToString(publicKey?.public?.encoded, Base64.DEFAULT))

        val registerInfo = apiInterface.register(registerRequest)

        if (registerInfo.error == 1) {
            throw Exception(registerInfo.message)
        }
    }


    suspend fun login(userName: String, passwordAuthentication: String, context: Context){
        val session = Session(context)

        //if one of the fields is empty, return
        if (userName.isEmpty() || passwordAuthentication.isEmpty()) {
            throw Exception("All fields must be filled")
        }

        val loginRequest = LoginRequest(userName,passwordAuthentication)
        val loginInfo = apiInterface.login(loginRequest)

        if (loginInfo.error == 1) {
            throw Exception(loginInfo.message)
        }

        if(loginInfo.error == 0){
            session.createSession(loginInfo.userId, loginInfo.supermarket_publickey)
        }
    }

    suspend fun voucher(userUUID: String?): List<Voucher> {
        val owner: UUID = UUID.fromString(userUUID)
        val voucherRequest = VoucherRequest(owner)
        val voucherInfo = apiInterface.voucher(voucherRequest)

        return voucherInfo.voucherList
    }

    suspend fun getTransactions(uuid: String): List<Transaction> {
        return apiInterface.getTransactionsByUserId(uuid).pastTransactionList
    }
}