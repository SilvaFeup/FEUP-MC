package fr.kodo.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import fr.kodo.myapplication.controller.AuthController
import fr.kodo.myapplication.model.Session
import kotlinx.coroutines.Dispatchers


import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.PasswordAuthentication

class LoginActivity : AppCompatActivity() {
    val loginController = AuthController()

    val edtNickname by lazy { findViewById<EditText>(R.id.login_edt_nickname) }
    val edtPassword by lazy { findViewById<EditText>(R.id.login_edt_password) }
    val btnLogin by lazy { findViewById<Button>(R.id.login_bt_login) }
    val btnRegister by lazy { findViewById<Button>(R.id.login_bt_register) }

    val session by lazy { Session(this) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        //supportActionBar?.hide()

        btnLogin.setOnClickListener {
            val nickname = edtNickname.text.toString()
            val password = edtPassword.text.toString()
            if (nickname.isEmpty() || password.isEmpty()) {
                return@setOnClickListener
            }
            else {
                loginUser(nickname,password)
            }
        }


        btnRegister.setOnClickListener {
            //Switch to RegisterActivity
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    fun loginUser(userName:String,password: String) {

        var responseCode = -1;

        lifecycleScope.launch {
            try {
                responseCode = loginController.login(userName,password, this@LoginActivity)
            } catch (e: Exception) {
                // Handle network or server error
                Log.println(Log.ERROR,"Error",e.stackTraceToString())
            }

            withContext(Dispatchers.Main){
                Log.println(Log.INFO,"Response Code",responseCode.toString())
                if(responseCode == 1){
                    //Switch to LoginActivity
                    val intent = Intent(this@LoginActivity, WelcomeActivity::class.java)
                    startActivity(intent)
                }
            }
        }
    }
}