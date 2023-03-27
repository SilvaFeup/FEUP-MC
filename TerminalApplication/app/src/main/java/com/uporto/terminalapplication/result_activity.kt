package com.uporto.terminalapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button

class result_activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        val button = findViewById<Button>(R.id.result_bt_back)


        //TODO set text of both messages

        button.setOnClickListener(object : View.OnClickListener{
            override fun onClick(p0: View?) {
                val mainIntent = Intent(this@result_activity,MainActivity::class.java)
                startActivity(mainIntent)
            }
        })
    }
}