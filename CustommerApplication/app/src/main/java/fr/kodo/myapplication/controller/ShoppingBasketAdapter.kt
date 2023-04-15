package fr.kodo.myapplication.controller

import android.app.AlertDialog
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import fr.kodo.myapplication.R
import fr.kodo.myapplication.model.Product
import java.text.DecimalFormat

class ShoppingBasketAdapter(private val shoppingBasket: ArrayList<Product>, val empty_message: TextView) : RecyclerView.Adapter<ShoppingBasketViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShoppingBasketViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.shopping_basket_item, parent, false)
        return ShoppingBasketViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return shoppingBasket.size
    }

    override fun onBindViewHolder(holder: ShoppingBasketViewHolder, position: Int) {
        val product = shoppingBasket[position]
        holder.name.text = product.name
        holder.price.text = product.price.toString() + "€"
        holder.quantity.setText(product.quantity.toString())

        val df = DecimalFormat("#.##")
        holder.totalPrice.text = df.format(product.price * product.quantity).toString() + "€"

        holder.deleteBt.setOnClickListener {
            val builder = AlertDialog.Builder(holder.itemView.context)
            builder.setTitle("Delete product")
            builder.setMessage("Are you sure you want to delete this product?")
            builder.setPositiveButton("OK") { dialog, which ->
                shoppingBasket.remove(product)
                notifyItemRemoved(position)
                if (shoppingBasket.isEmpty()){
                    empty_message.visibility = View.VISIBLE
                }
            }
            builder.setNegativeButton("Cancel") { dialog, which -> }
            builder.show()
        }
        holder.quantity.setOnKeyListener { _, _, _ ->
            if (holder.quantity.text.toString() != "") {
                product.quantity = holder.quantity.text.toString().toInt()
                holder.totalPrice.text = df.format(product.price * product.quantity).toString() + "€"
            }
            false
        }
    }
}