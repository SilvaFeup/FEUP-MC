package fr.kodo.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import fr.kodo.myapplication.controller.AuthController
import fr.kodo.myapplication.crypto.KeyStoreUtils
import kotlinx.coroutines.Dispatchers


import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.security.Signature

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

            //TODO: remove this
            //verify this signature: 010c1775aa1fee59349c6d9a3c169ec3f3878b1f24307161aa80144f3a0ca8ad48af7114aaacdccc07315a1a2c5d4548047bcf17f791e0b36a6704be4cd83fe5
            val signature = Signature.getInstance("SHA256withRSA")
            signature.initVerify(KeyStoreUtils.getKeyPair(nickname)?.public)
            signature.update("807e8a08-d7b6-11ed-afa1-0242ac120002,1,68443576-6e4d-4a63-93f0-6d846fe772df,0,0".toByteArray())
            val result = signature.verify(Base64.decode("010c1775aa1fee59349c6d9a3c169ec3f3878b1f24307161aa80144f3a0ca8ad48af7114aaacdccc07315a1a2c5d4548047bcf17f791e0b36a6704be4cd83fe5",Base64.DEFAULT))
            Log.e("signature",result.toString())

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