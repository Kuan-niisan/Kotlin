package com.example.calculator // Thay bằng package của bạn

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

class CalculatorFragment : Fragment() {

    private lateinit var tvResult: TextView
    private lateinit var tvHistory: TextView
    // ... các biến trạng thái khác ...
    private var operand1: Double? = null
    private var pendingOperation = ""
    private var isNewNumber = true


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout cho Fragment này
        return inflater.inflate(R.layout.fragment_calculator, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Đặt tiêu đề cho Toolbar
        (requireActivity() as AppCompatActivity).supportActionBar?.title = "Standard"

        // Logic code được chuyển vào đây, và dùng "view.findViewById"
        tvResult = view.findViewById(R.id.tvResult)
        tvHistory = view.findViewById(R.id.tvHistory)

        val numberClickListener = View.OnClickListener { v ->
            val button = v as Button
            val number = button.text.toString()

            if (isNewNumber) {
                tvResult.text = number
                isNewNumber = false
            } else {
                if (tvResult.text == "0") {
                    tvResult.text = number
                } else {
                    tvResult.append(number)
                }
            }
        }
        // ... Dán toàn bộ logic xử lý nút bấm từ file CalculatorActivity cũ vào đây ...
        // Đảm bảo thay tất cả "findViewById" thành "view.findViewById"

        val operatorClickListener = View.OnClickListener { v ->
            val button = v as Button
            val operator = button.text.toString()
            val value = tvResult.text.toString().toDoubleOrNull() ?: return@OnClickListener

            if (operand1 != null && !isNewNumber) {
                val result = performOperation(operand1!!, value, pendingOperation)
                displayResult(result)
                operand1 = result
            } else {
                operand1 = value
            }

            pendingOperation = operator
            isNewNumber = true
            tvHistory.text = "${formatNumber(operand1!!)} $pendingOperation"
        }

        view.findViewById<Button>(R.id.button_equals).setOnClickListener {
            val value = tvResult.text.toString().toDoubleOrNull() ?: return@setOnClickListener

            if (operand1 != null && pendingOperation.isNotEmpty() && !isNewNumber) {
                tvHistory.text = "${formatNumber(operand1!!)} $pendingOperation ${formatNumber(value)} ="
                val result = performOperation(operand1!!, value, pendingOperation)
                displayResult(result)
                resetOperationState()
            }
        }

        view.findViewById<Button>(R.id.button_c).setOnClickListener {
            resetOperationState()
            tvResult.text = "0"
            tvHistory.text = ""
        }

        view.findViewById<Button>(R.id.button_ce).setOnClickListener {
            tvResult.text = "0"
            isNewNumber = true
        }

        view.findViewById<Button>(R.id.button_bs).setOnClickListener {
            if (!isNewNumber) {
                val currentText = tvResult.text.toString()
                if (currentText.length > 1) {
                    tvResult.text = currentText.dropLast(1)
                } else {
                    tvResult.text = "0"
                }
            }
        }

        attachClickListeners(view, numberClickListener, operatorClickListener)
    }

    private fun attachClickListeners(view: View, numberListener: View.OnClickListener, operatorListener: View.OnClickListener) {
        view.findViewById<Button>(R.id.button_zero).setOnClickListener(numberListener)
        view.findViewById<Button>(R.id.button_one).setOnClickListener(numberListener)
        view.findViewById<Button>(R.id.button_two).setOnClickListener(numberListener)
        view.findViewById<Button>(R.id.button_three).setOnClickListener(numberListener)
        view.findViewById<Button>(R.id.button_four).setOnClickListener(numberListener)
        view.findViewById<Button>(R.id.button_five).setOnClickListener(numberListener)
        view.findViewById<Button>(R.id.button_six).setOnClickListener(numberListener)
        view.findViewById<Button>(R.id.button_seven).setOnClickListener(numberListener)
        view.findViewById<Button>(R.id.button_eight).setOnClickListener(numberListener)
        view.findViewById<Button>(R.id.button_nine).setOnClickListener(numberListener)

        view.findViewById<Button>(R.id.button_add).setOnClickListener(operatorListener)
        view.findViewById<Button>(R.id.button_subtract).setOnClickListener(operatorListener)
        view.findViewById<Button>(R.id.button_multiply).setOnClickListener(operatorListener)
        view.findViewById<Button>(R.id.button_divide).setOnClickListener(operatorListener)
    }

    private fun formatNumber(number: Double): String {
        return if (number % 1.0 == 0.0) number.toLong().toString() else number.toString()
    }

    private fun displayResult(result: Double) {
        tvResult.text = if (result.isNaN()) "Error" else formatNumber(result)
    }

    private fun resetOperationState() {
        operand1 = null
        pendingOperation = ""
        isNewNumber = true
    }

    private fun performOperation(op1: Double, op2: Double, operation: String): Double {
        return when (operation) {
            "+" -> op1 + op2
            "-" -> op1 - op2
            "x" -> op1 * op2
            "/" -> if (op2 != 0.0) op1 / op2 else Double.NaN
            else -> 0.0
        }
    }
}