package com.uporto.terminalapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val button = findViewById<Button>(R.id.main_bt_checkout)

        button.setOnClickListener(object : View.OnClickListener{
            override fun onClick(p0 : View?){
                val resultIntent = Intent(this@MainActivity,result_activity::class.java)
                startActivity(resultIntent)
            }
        })
    }
}