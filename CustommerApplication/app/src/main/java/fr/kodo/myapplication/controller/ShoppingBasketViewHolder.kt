package fr.kodo.myapplication.controller

import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import fr.kodo.myapplication.R

class ShoppingBasketViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
    val name = itemView.findViewById<TextView>(R.id.shopping_basket_product_name)
    val price = itemView.findViewById<TextView>(R.id.shopping_basket_product_price)
    val quantity = itemView.findViewById<EditText>(R.id.shopping_basket_product_quantity)
    val totalPrice = itemView.findViewById<TextView>(R.id.shopping_basket_product_total_price)
    val deleteBt = itemView.findViewById<ImageButton>(R.id.shopping_basket_product_bt_delete)
}