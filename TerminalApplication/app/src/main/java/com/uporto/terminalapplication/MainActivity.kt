package com.uporto.terminalapplication

import android.app.Activity
import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.uporto.terminalapplication.controller.CheckoutController
import kotlinx.coroutines.launch
import java.util.UUID

private const val ACTION_SCAN = "com.google.zxing.client.android.SCAN"

class MainActivity : AppCompatActivity() {
    private val button by lazy{ findViewById<Button>(R.id.main_bt_checkout) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button.setOnClickListener{scan()}
    }

    // If we have not the scan app on the device
    private fun showDialog(act: Activity, title: CharSequence, message: CharSequence, buttonYes: CharSequence, buttonNo: CharSequence): AlertDialog {
        val downloadDialog = AlertDialog.Builder(act)
        downloadDialog.setTitle(title)
        downloadDialog.setMessage(message)
        downloadDialog.setPositiveButton(buttonYes) { _, _ ->
            val uri = Uri.parse("market://search?q=pname:com.google.zxing.client.android")
            val intent = Intent(Intent.ACTION_VIEW, uri)
            act.startActivity(intent)
        }
        downloadDialog.setNegativeButton(buttonNo, null)
        return downloadDialog.create()
    }

    private fun scan(){
        try {
            val intent = Intent(ACTION_SCAN).apply {
                putExtra("SCAN_MOD", "QR_CODE_MODE")}
            startActivityForResult(intent,0)
        }
        catch (e: ActivityNotFoundException){
            showDialog(this, "No Scanner Found", "Download a scanner code app?", "Yes", "No").show()
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0){
            if (resultCode == RESULT_OK){
                makeRequest(data?.getStringExtra("SCAN_RESULT")?:"")
            }
        }
    }

    private fun makeRequest(str: String){
        if (str.isEmpty()){
            throw Exception("empty basket!")
        }
        Log.e("basket", str)
        var array = str.split(",")

        //the QR-code must contain at least the userId, the bool for discount and the voucherId
        if (array.size < 3) {
            Log.e("Error basket size", "the QR-code transmit less than 3 information!\n information are :\t $str")
            return;
        }

        var idProductList : ArrayList<UUID> = ArrayList()
        var productQuantityList : ArrayList<Int> = ArrayList()
        var userId = UUID.fromString(array[array.size-3])
        var useAccumulatedDiscount = array[array.size-2].toFloat()
        var voucherId = array[array.size-1].toInt()

        for (i in 0..array.size-4 step 2){
            idProductList.add(UUID.fromString(array[i]))
            productQuantityList.add(array[i+1].toInt())
        }
        val checkoutController = CheckoutController()

        lifecycleScope.launch{
            try {
                var response = checkoutController.checkout(idProductList, productQuantityList, userId, useAccumulatedDiscount, voucherId)

                var intent = Intent(this@MainActivity,result_activity::class.java)

            if(response[0].toDouble()>=0f){
                intent.putExtra("valid",true)
                intent.putExtra("total",response[0])
                intent.putExtra("discount",response[1])

                Log.e("total", response[0])
            }
            else{
                intent.putExtra("valid",false)
                Toast.makeText(this@MainActivity,response[2],Toast.LENGTH_LONG).show()
            }
                startActivity(intent)
            }
            catch (e: Exception){
                Log.e("Error", e.stackTraceToString())
            }
        }
    }
}