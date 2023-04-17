package fr.kodo.myapplication

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import fr.kodo.myapplication.controller.AuthController
import fr.kodo.myapplication.controller.SeeVoucherAdapter
import fr.kodo.myapplication.controller.Session
import fr.kodo.myapplication.model.Voucher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SeeVoucherFragment: DialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_see_voucher, container, false)

        val authController = AuthController()
        val session = Session(requireContext())

        var voucherList: List<Voucher>? = null
        lifecycleScope.launch {
            try {
                voucherList = authController.voucher(session.getUserUUID())
            } catch (e: Exception) {
                // Handle network or server error
                Log.e("SeeVoucherFragment", e.stackTraceToString())
                Toast.makeText(requireContext(), "${e.message}", Toast.LENGTH_LONG).show()
            }

            withContext(Dispatchers.Main){

                val recyclerView = view.findViewById<RecyclerView>(R.id.see_voucher_recycler_view)
                if (voucherList != null && voucherList!!.isNotEmpty()) {
                    recyclerView.adapter = SeeVoucherAdapter(voucherList!!)
                    recyclerView.layoutManager = LinearLayoutManager(requireContext())
                }
                else{
                    view.findViewById<TextView>(R.id.see_voucher_tv_empty).visibility = TextView.VISIBLE
                }


                //progressBar.visibility = ProgressBar.GONE
                //btnLogin.isEnabled = true
            }
        }



        view.findViewById<Button>(R.id.see_voucher_bt_done).setOnClickListener {
            dismiss()
        }

        return view
    }
}