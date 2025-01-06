package com.example.pennywise

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment

class CalculatorFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_calculator, container, false)

        // Initialize UI elements
        val inputPV = view.findViewById<EditText>(R.id.input_pv)
        val inputT = view.findViewById<EditText>(R.id.input_t)
        val inputRate = view.findViewById<EditText>(R.id.input_rate)
        val inputM = view.findViewById<EditText>(R.id.input_m)
        val btnCalculate = view.findViewById<Button>(R.id.btn_calculate)
        val btnClear = view.findViewById<Button>(R.id.btn_clear)
        val resultTextView = view.findViewById<TextView>(R.id.result_text_view)

        val activity = activity as? MainActivity

        // Focus change listener to hide/show navigation bar
        val hideNavBarListener = View.OnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                activity?.hideNavigationBar()
            } else {
                activity?.showNavigationBar()
            }
        }

        // Apply focus change listener to input fields
        listOf(inputPV, inputT, inputRate, inputM).forEach {
            it.onFocusChangeListener = hideNavBarListener
        }

        // Adjust layout based on keyboard visibility
        ViewCompat.setOnApplyWindowInsetsListener(view) { _, insets ->
            val imeVisible = insets.isVisible(WindowInsetsCompat.Type.ime())
            if (imeVisible) activity?.hideNavigationBar() else activity?.showNavigationBar()
            WindowInsetsCompat.CONSUMED
        }

        // Calculate button click listener
        btnCalculate.setOnClickListener {
            val pv = inputPV.text.toString().toDoubleOrNull()
            val t = inputT.text.toString().toDoubleOrNull()
            val rate = inputRate.text.toString().toDoubleOrNull()
            val m = inputM.text.toString().toDoubleOrNull()

            if (pv == null || t == null || rate == null || m == null || m < 0) {
                Toast.makeText(activity, "Please enter valid inputs", Toast.LENGTH_SHORT).show()
                resultTextView.text = "₱00.00"
                return@setOnClickListener
            }

            try {
                val fv = if (m == 0.0) {
                    pv * (1 + (rate / 100) * t) // Simple Interest
                } else {
                    pv * Math.pow((1 + (rate / 100) / m), m * t) // Compound Interest
                }
                resultTextView.text = String.format("₱%.2f", fv)
            } catch (e: Exception) {
                Toast.makeText(activity, "Error during calculation", Toast.LENGTH_SHORT).show()
                resultTextView.text = "₱00.00"
            }
        }

        // Clear button click listener
        btnClear.setOnClickListener {
            listOf(inputPV, inputT, inputRate, inputM).forEach { it.text.clear() }
            resultTextView.text = "₱00.00"
        }

        return view
    }
}
