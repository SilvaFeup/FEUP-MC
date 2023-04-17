package fr.kodo.myapplication.controller

import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import fr.kodo.myapplication.R

class ShoppingBasketViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
    val name:TextView = itemView.findViewById(R.id.shopping_basket_product_name)
    val price:TextView = itemView.findViewById(R.id.shopping_basket_product_price)
    val quantity:EditText = itemView.findViewById(R.id.shopping_basket_product_quantity)
    val totalPrice:TextView = itemView.findViewById(R.id.shopping_basket_product_total_price)
    val deleteBt:ImageButton = itemView.findViewById(R.id.shopping_basket_product_bt_delete)
}