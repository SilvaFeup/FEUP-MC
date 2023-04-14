package fr.kodo.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import fr.kodo.myapplication.controller.AuthController
import fr.kodo.myapplication.controller.TransactionAdapter
import fr.kodo.myapplication.model.Session
import fr.kodo.myapplication.model.Transaction
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.awaitResponse
import retrofit2.converter.gson.GsonConverterFactory

class PastTransactionActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private var transactionList = ArrayList<Transaction>()

    private val authController by lazy { AuthController() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_past_transaction)

        recyclerView = findViewById(R.id.transaction_list)
        recyclerView.adapter = TransactionAdapter(transactionList)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val uuid = Session.KEY_USER_UUID

        lifecycleScope.launch {
            transactionList = authController.getTransactions(uuid)
            recyclerView.adapter?.notifyItemRangeInserted(0, transactionList.size)
        }
    }
}



