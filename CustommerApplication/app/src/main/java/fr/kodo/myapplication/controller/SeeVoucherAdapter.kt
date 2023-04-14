package fr.kodo.myapplication.controller

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import fr.kodo.myapplication.R
import fr.kodo.myapplication.model.Voucher

class SeeVoucherAdapter(val voucherList: List<Voucher>): RecyclerView.Adapter<SeeVoucherViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SeeVoucherViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.voucher_item, parent, false)
        return SeeVoucherViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return voucherList.size
    }

    override fun onBindViewHolder(holder: SeeVoucherViewHolder, position: Int) {
        val voucher = voucherList[position]
        holder.id.text = voucher.id.toString()

    }
}