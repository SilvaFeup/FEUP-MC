package fr.kodo.myapplication.controller

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

import fr.kodo.myapplication.R

import fr.kodo.myapplication.model.Transaction



class TransactionAdapter(private val transactions: MutableList<Transaction>) :

    RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.transaction_item, parent, false)
        return TransactionViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {

        val transaction = transactions[position]

        // Bind the transaction data to the corresponding views in the layout
        holder.totalAmount.text = "Total Amount: $${transaction.total_amount}"
        if (transaction.voucher_id == -1) {
            holder.voucherId.text = "Voucher ID: No voucher"
        } else{
            holder.voucherId.text = "Voucher ID: ${transaction.voucher_id}"
        }
        holder.dateOrder.text = "Date Order: ${transaction.date_order}"
    }

    override fun getItemCount() = transactions.size

    inner class TransactionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val totalAmount: TextView = itemView.findViewById(R.id.total_amount)
        val voucherId: TextView = itemView.findViewById(R.id.voucher_id)
        val dateOrder: TextView = itemView.findViewById(R.id.date_order)
    }
}
