package fr.kodo.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import fr.kodo.myapplication.controller.AuthController
import fr.kodo.myapplication.controller.TransactionAdapter
import fr.kodo.myapplication.model.Session
import fr.kodo.myapplication.model.Transaction
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PastTransactionActivity : AppCompatActivity() {

    private val authController by lazy { AuthController() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_past_transaction)

        findViewById<Button>(R.id.past_transaction_bt_back).setOnClickListener{
            finish()
        }

        var recyclerView: RecyclerView

        val uuid = Session(this@PastTransactionActivity).getUserUUID()

        try{
            lifecycleScope.launch(Dispatchers.IO) {
                val transactionList = authController.getTransactions(uuid)
                Log.e("TransactionList", transactionList.toString())

                withContext(Dispatchers.Main) {

                    if (transactionList.isEmpty()){
                        findViewById<TextView>(R.id.past_transaction_tv_empty).visibility = TextView.VISIBLE
                    }
                    else {
                        recyclerView = findViewById(R.id.transaction_list)
                        recyclerView.adapter = TransactionAdapter(transactionList as MutableList<Transaction>)
                        recyclerView.layoutManager = LinearLayoutManager(this@PastTransactionActivity)
                    }
                }
            }
        }
        catch (e: Exception){
            Toast.makeText(this, "Error: ${e.stackTrace}", Toast.LENGTH_LONG).show()
            Log.e("Error", e.stackTrace.toString())
        }
    }
}



