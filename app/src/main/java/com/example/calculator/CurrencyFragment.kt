package com.example.calculator

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.fragment.app.Fragment

data class Currency(val code: String, val name: String, val symbol: String)

class CurrencyFragment : Fragment() {

    private lateinit var etAmountFrom: EditText
    private lateinit var etAmountTo: EditText
    private lateinit var spinnerFrom: Spinner
    private lateinit var spinnerTo: Spinner
    private lateinit var tvFromSymbol: TextView
    private lateinit var tvToSymbol: TextView

    private var activeEditText: EditText? = null
    private var isUpdating = false

    private val exchangeRates = mapOf(
        "USD" to 1.0, "VND" to 25455.0, "EUR" to 0.92, "JPY" to 157.6, "GBP" to 0.79,
        "AUD" to 1.51, "CAD" to 1.37, "CHF" to 0.90, "CNY" to 7.25, "HKD" to 7.81,
        "KRW" to 1380.5, "INR" to 83.5, "RUB" to 90.0, "BRL" to 5.15, "ZAR" to 18.7
    )

    private val currencyList = listOf(
        Currency("AUD", "Australia - Dollar", "$"), Currency("BRL", "Brazil - Real", "R$"),
        Currency("CAD", "Canada - Dollar", "$"), Currency("CHF", "Switzerland - Franc", "Fr"),
        Currency("CNY", "China - Yuan", "¥"), Currency("EUR", "Eurozone - Euro", "€"),
        Currency("GBP", "United Kingdom - Pound", "£"), Currency("HKD", "Hong Kong - Dollar", "$"),
        Currency("INR", "India - Rupee", "₹"), Currency("JPY", "Japan - Yen", "¥"),
        Currency("KRW", "South Korea - Won", "₩"), Currency("RUB", "Russia - Ruble", "₽"),
        Currency("USD", "United States - Dollar", "$"), Currency("VND", "Vietnam - Dong", "₫"),
        Currency("ZAR", "South Africa - Rand", "R")
    ).sortedBy { it.name }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_currency, container, false)

        etAmountFrom = root.findViewById(R.id.etAmountFrom)
        etAmountTo = root.findViewById(R.id.etAmountTo)
        spinnerFrom = root.findViewById(R.id.spinnerFrom)
        spinnerTo = root.findViewById(R.id.spinnerTo)
        tvFromSymbol = root.findViewById(R.id.tvFromSymbol)
        tvToSymbol = root.findViewById(R.id.tvToSymbol)

        val currencyNames = currencyList.map { "${it.name} (${it.code})" }
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, currencyNames)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerFrom.adapter = adapter
        spinnerTo.adapter = adapter

        spinnerFrom.setSelection(currencyNames.indexOf("Japan - Yen (JPY)"))
        spinnerTo.setSelection(currencyNames.indexOf("Vietnam - Dong (VND)"))

        setupListeners(root)
        activeEditText = etAmountFrom

        return root
    }

    private fun setupListeners(root: View) {
        etAmountFrom.setOnFocusChangeListener { _, hasFocus -> if (hasFocus) activeEditText = etAmountFrom }
        etAmountTo.setOnFocusChangeListener { _, hasFocus -> if (hasFocus) activeEditText = etAmountTo }

        val spinnerListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                updateSymbols()
                convertCurrency()
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
        spinnerFrom.onItemSelectedListener = spinnerListener
        spinnerTo.onItemSelectedListener = spinnerListener

        val numpadClickListener = View.OnClickListener { v ->
            val button = v as Button
            val input = button.text.toString()
            activeEditText?.let {
                val currentText = it.text.toString()
                if (currentText == "0") it.setText(input) else it.append(input)
            }
        }

        // --- CẬP NHẬT CÁC ID Ở ĐÂY ---
        root.findViewById<Button>(R.id.button_zero).setOnClickListener(numpadClickListener)
        root.findViewById<Button>(R.id.button_one).setOnClickListener(numpadClickListener)
        root.findViewById<Button>(R.id.button_two).setOnClickListener(numpadClickListener)
        root.findViewById<Button>(R.id.button_three).setOnClickListener(numpadClickListener)
        root.findViewById<Button>(R.id.button_four).setOnClickListener(numpadClickListener)
        root.findViewById<Button>(R.id.button_five).setOnClickListener(numpadClickListener)
        root.findViewById<Button>(R.id.button_six).setOnClickListener(numpadClickListener)
        root.findViewById<Button>(R.id.button_seven).setOnClickListener(numpadClickListener)
        root.findViewById<Button>(R.id.button_eight).setOnClickListener(numpadClickListener)
        root.findViewById<Button>(R.id.button_nine).setOnClickListener(numpadClickListener)

        root.findViewById<Button>(R.id.button_ce).setOnClickListener {
            activeEditText?.setText("0")
        }
        root.findViewById<Button>(R.id.button_bs).setOnClickListener {
            activeEditText?.let {
                val currentText = it.text.toString()
                if (currentText.isNotEmpty()) it.setText(currentText.dropLast(1))
                if (it.text.isEmpty()) it.setText("0")
            }
        }

        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!isUpdating) convertCurrency()
            }
            override fun afterTextChanged(s: Editable?) {}
        }
        etAmountFrom.addTextChangedListener(textWatcher)
        etAmountTo.addTextChangedListener(textWatcher)
    }

    private fun convertCurrency() {
        if (activeEditText == null) return

        isUpdating = true

        val sourceEt = if (activeEditText == etAmountFrom) etAmountFrom else etAmountTo
        val targetEt = if (activeEditText == etAmountFrom) etAmountTo else etAmountFrom
        val sourceSpinner = if (activeEditText == etAmountFrom) spinnerFrom else spinnerTo
        val targetSpinner = if (activeEditText == etAmountFrom) spinnerTo else spinnerFrom

        val amountStr = sourceEt.text.toString()
        if (amountStr.isNotEmpty() && amountStr != ".") {
            val amount = amountStr.toDouble()

            val fromCurrencyCode = currencyList[sourceSpinner.selectedItemPosition].code
            val toCurrencyCode = currencyList[targetSpinner.selectedItemPosition].code

            val fromRate = exchangeRates[fromCurrencyCode]!!
            val toRate = exchangeRates[toCurrencyCode]!!

            val result = amount / fromRate * toRate
            targetEt.setText(String.format("%.2f", result))
        } else if (amountStr.isEmpty()) {
            targetEt.setText("")
        }

        isUpdating = false
    }

    private fun updateSymbols() {
        tvFromSymbol.text = currencyList[spinnerFrom.selectedItemPosition].symbol
        tvToSymbol.text = currencyList[spinnerTo.selectedItemPosition].symbol
    }
}