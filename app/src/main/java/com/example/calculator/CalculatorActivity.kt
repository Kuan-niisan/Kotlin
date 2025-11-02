package com.example.calculator

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class CalculatorActivity : AppCompatActivity() {

    private lateinit var tvResult: TextView
    private lateinit var tvHistory: TextView

    private var operand1: Double? = null
    private var pendingOperation = ""
    private var isNewNumber = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calculator)

        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayUseLogoEnabled(true)
        supportActionBar?.setLogo(R.mipmap.ic_launcher_round)

        tvResult = findViewById(R.id.tvResult)
        tvHistory = findViewById(R.id.tvHistory)

        val numberClickListener = View.OnClickListener { view ->
            val button = view as Button
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

        val operatorClickListener = View.OnClickListener { view ->
            val button = view as Button
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

        findViewById<Button>(R.id.button_equals).setOnClickListener {
            val value = tvResult.text.toString().toDoubleOrNull() ?: return@setOnClickListener

            if (operand1 != null && pendingOperation.isNotEmpty()) {
                tvHistory.text = "${formatNumber(operand1!!)} $pendingOperation ${formatNumber(value)} ="
                val result = performOperation(operand1!!, value, pendingOperation)
                displayResult(result)
                resetOperationState()
            }
        }

        findViewById<Button>(R.id.button_c).setOnClickListener {
            resetOperationState()
            tvResult.text = "0"
            tvHistory.text = ""
        }

        findViewById<Button>(R.id.button_ce).setOnClickListener {
            tvResult.text = "0"
            isNewNumber = true
        }

        findViewById<Button>(R.id.button_bs).setOnClickListener {
            if (!isNewNumber) {
                val currentText = tvResult.text.toString()
                if (currentText.length > 1) {
                    tvResult.text = currentText.dropLast(1)
                } else {
                    tvResult.text = "0"
                }
            }
        }

        attachClickListeners(numberClickListener, operatorClickListener)
    }

    private fun attachClickListeners(numberListener: View.OnClickListener, operatorListener: View.OnClickListener) {
        findViewById<Button>(R.id.button_zero).setOnClickListener(numberListener)
        findViewById<Button>(R.id.button_one).setOnClickListener(numberListener)
        findViewById<Button>(R.id.button_two).setOnClickListener(numberListener)
        findViewById<Button>(R.id.button_three).setOnClickListener(numberListener)
        findViewById<Button>(R.id.button_four).setOnClickListener(numberListener)
        findViewById<Button>(R.id.button_five).setOnClickListener(numberListener)
        findViewById<Button>(R.id.button_six).setOnClickListener(numberListener)
        findViewById<Button>(R.id.button_seven).setOnClickListener(numberListener)
        findViewById<Button>(R.id.button_eight).setOnClickListener(numberListener)
        findViewById<Button>(R.id.button_nine).setOnClickListener(numberListener)

        findViewById<Button>(R.id.button_add).setOnClickListener(operatorListener)
        findViewById<Button>(R.id.button_subtract).setOnClickListener(operatorListener)
        findViewById<Button>(R.id.button_multiply).setOnClickListener(operatorListener)
        findViewById<Button>(R.id.button_divide).setOnClickListener(operatorListener)

        findViewById<Button>(R.id.button_plus_minus).setOnClickListener { }
        findViewById<Button>(R.id.button_dot).setOnClickListener { }
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

    private fun formatNumber(number: Double): String {
        return if (number % 1.0 == 0.0) {
            number.toLong().toString()
        } else {
            number.toString()
        }
    }

    private fun displayResult(result: Double) {
        if (result.isNaN()) {
            tvResult.text = "Error"
        } else {
            tvResult.text = formatNumber(result)
        }
    }

    private fun resetOperationState() {
        operand1 = null
        pendingOperation = ""
        isNewNumber = true
    }
}