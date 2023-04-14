package fr.kodo.myapplication.controller

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import fr.kodo.myapplication.APIInterface
import fr.kodo.myapplication.R
import fr.kodo.myapplication.model.Transaction
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class TransactionAdapter(private val context: Context) :
    RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder>() {

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


    private var transactions: List<Transaction> = emptyList()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.transaction_item, parent, false)
        return TransactionViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        val transaction = transactions[position]

        // Bind the transaction data to the corresponding views in the layout
        holder.totalAmount.text = "Total Amount: $${transaction.totalAmount}"
        holder.voucherId.text = "Voucher ID: ${transaction.voucherId}"
        holder.dateOrder.text = "Date Order: ${transaction.dateOrder}"
    }

    override fun getItemCount() = transactions.size

    inner class TransactionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val totalAmount: TextView = itemView.findViewById(R.id.total_amount)
        val voucherId: TextView = itemView.findViewById(R.id.voucher_id)
        val dateOrder: TextView = itemView.findViewById(R.id.date_order)
    }
}
