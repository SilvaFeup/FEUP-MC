package fr.kodo.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import fr.kodo.myapplication.controller.AuthController
import kotlinx.coroutines.Dispatchers


import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginActivity : AppCompatActivity() {
    private val authController = AuthController()

    private val edtNickname by lazy { findViewById<EditText>(R.id.login_edt_nickname) }
    private val edtPassword by lazy { findViewById<EditText>(R.id.login_edt_password) }
    private val btnLogin by lazy { findViewById<Button>(R.id.login_bt_login) }
    private val btnRegister by lazy { findViewById<Button>(R.id.login_bt_register) }

    private val progressBar: ProgressBar by lazy { findViewById(R.id.login_progress_bar) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        //supportActionBar?.hide()

        btnLogin.setOnClickListener {
            val nickname = edtNickname.text.toString()
            val password = edtPassword.text.toString()

            progressBar.visibility = ProgressBar.VISIBLE
            btnLogin.isEnabled = false
            loginUser(nickname,password)
        }


        btnRegister.setOnClickListener {
            //Switch to RegisterActivity
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun loginUser(userName:String,password: String) {

        lifecycleScope.launch {
            try {
                authController.login(userName,password, this@LoginActivity)
            } catch (e: Exception) {
                // Handle network or server error
                Log.println(Log.ERROR,"Error",e.stackTraceToString())
                Toast.makeText(this@LoginActivity, "${e.message}", Toast.LENGTH_LONG).show()
                progressBar.visibility = ProgressBar.GONE
                btnLogin.isEnabled = true
                return@launch
            }

            withContext(Dispatchers.Main){
                //Switch to LoginActivity
                val intent = Intent(this@LoginActivity, WelcomeActivity::class.java)
                startActivity(intent)

                progressBar.visibility = ProgressBar.GONE
                btnLogin.isEnabled = true
            }
        }
    }
}