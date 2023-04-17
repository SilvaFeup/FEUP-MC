package fr.kodo.myapplication.controller

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import fr.kodo.myapplication.R

class SeeVoucherViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    val id:TextView = itemView.findViewById(R.id.voucher_item_tv_voucher_id)
}