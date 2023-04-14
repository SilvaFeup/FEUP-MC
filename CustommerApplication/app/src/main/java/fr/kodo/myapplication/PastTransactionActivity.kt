package fr.kodo.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import fr.kodo.myapplication.controller.TransactionAdapter
import fr.kodo.myapplication.model.Session
import fr.kodo.myapplication.model.Transaction
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.awaitResponse
import retrofit2.converter.gson.GsonConverterFactory

class PastTransactionActivity : AppCompatActivity() {

    val apiInterface: APIInterface by lazy {
        Retrofit.Builder()
            //.baseUrl("http://192.168.1.81:3000/")//Axel
            .baseUrl("http://10.0.2.2:3000/")//emulator
            //.baseUrl("http://192.168.1.80:3000/")//aurélien
            //.baseUrl("http://192.168.250.163:3000/")//aurélien with his own connexion
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(APIInterface::class.java)
    }

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: TransactionAdapter
    private var transactionList = mutableListOf<Transaction>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_past_transaction)

        recyclerView = findViewById(R.id.transaction_list)
        adapter = TransactionAdapter(transactionList)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        val uuid = Session.KEY_USER_UUID

        lifecycleScope.launch {
            val response = apiInterface.getTransactionsByUserId(uuid).awaitResponse()

            if (response.isSuccessful) {
                val json = response.body()?.string()
                transactionList.addAll(Gson().fromJson(json, object : TypeToken<List<Transaction>>() {}.type))
                adapter.notifyDataSetChanged()
            } else {
                Toast.makeText(this@PastTransactionActivity, "Error retrieving transactions", Toast.LENGTH_SHORT).show()
            }
        }
    }
}



