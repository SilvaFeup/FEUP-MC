package com.uporto.terminalapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import org.w3c.dom.Text

class result_activity : AppCompatActivity() {
    private val button by lazy { findViewById<Button>(R.id.result_bt_back)}
    private val tv_result by lazy { findViewById<TextView>(R.id.result_tv_outcome) }
    private val tv_gate by lazy { findViewById<TextView>(R.id.result_tv_doorMessage) }
    private val tv_thanks by lazy { findViewById<TextView>(R.id.result_tv_thanks) }
    private val tv_total by lazy { findViewById<TextView>(R.id.result_tv_total) }
    private val tv_accumulatedDiscount by lazy { findViewById<TextView>(R.id.result_tv_amountAccumulated) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        if(intent.getBooleanExtra("valid",false)){
            tv_result.setText(R.string.positive_result_text)
            tv_gate.setText(R.string.open_gate)

            var total = intent.getStringExtra("total")
            tv_total.text = "Total payed :\t$total€"

            var discount = intent.getStringExtra("discount")
            tv_accumulatedDiscount.text = "You have $discount€ left in your account."
        }
        else{
            tv_result.setText(R.string.negative_result_text)
            tv_gate.setText(R.string.closed_gate)
            tv_thanks.text = null
        }

        button.setOnClickListener {
            val mainIntent = Intent(this@result_activity, MainActivity::class.java)
            startActivity(mainIntent)
        }
    }
}
