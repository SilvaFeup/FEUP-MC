package fr.kodo.myapplication

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.DialogFragment
import fr.kodo.myapplication.model.Product
import java.util.UUID


class OrderInfoFragment: DialogFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        var view : View = inflater.inflate(R.layout.fragment_order_info, container, false)

        view.findViewById<Button>(R.id.order_info_cancel_button).setOnClickListener {
            dismiss()
        }

        view.findViewById<Button>(R.id.order_info_confirm_button).setOnClickListener {
            arguments?.let {
                val message = it.getString("message")
                val totalPrice = it.getDouble("totalPrice")
                val userUUID = it.getString("userUUID")

                Log.println(Log.WARN, "OrderInfoFragment", "message: $message totalPrice: $totalPrice userUUID: $userUUID")

            }
        }

        return view
    }
}