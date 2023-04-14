package fr.kodo.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.DialogFragment

class SeeVoucherFragment: DialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_see_voucher, container, false)

        view.findViewById<Button>(R.id.see_voucher_bt_done).setOnClickListener {
            dismiss()
        }

        return view
    }
}