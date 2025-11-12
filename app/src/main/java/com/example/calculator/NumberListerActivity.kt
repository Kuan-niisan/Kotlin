package com.example.calculator

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ListView
import android.widget.RadioButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.sqrt

class NumberListerActivity : AppCompatActivity() {

    private lateinit var etNumberLimit: EditText
    private lateinit var lvResults: ListView
    private lateinit var tvNoResults: TextView
    private lateinit var adapter: ArrayAdapter<Int>

    // Tạo một danh sách để quản lý tất cả các RadioButton
    private lateinit var radioButtons: List<RadioButton>
    private var selectedRadioButtonId: Int = R.id.rbOdd // Lưu ID của nút được chọn

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_number_lister)

        etNumberLimit = findViewById(R.id.etNumberLimit)
        lvResults = findViewById(R.id.lvResults)
        tvNoResults = findViewById(R.id.tvNoResults)

        // Khởi tạo danh sách RadioButton
        radioButtons = listOf(
            findViewById(R.id.rbOdd),
            findViewById(R.id.rbPrime),
            findViewById(R.id.rbPerfect),
            findViewById(R.id.rbEven),
            findViewById(R.id.rbSquare),
            findViewById(R.id.rbFibonacci)
        )

        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, mutableListOf())
        lvResults.adapter = adapter

        // Tạo một bộ lắng nghe sự kiện chung cho tất cả RadioButton
        val radioButtonClickListener = View.OnClickListener { view ->
            val clickedButton = view as RadioButton

            // Bỏ chọn tất cả các nút khác
            radioButtons.forEach { button ->
                if (button.id != clickedButton.id) {
                    button.isChecked = false
                }
            }
            // Đảm bảo nút được nhấn luôn được chọn
            clickedButton.isChecked = true
            selectedRadioButtonId = clickedButton.id
            updateList()
        }

        // Gán bộ lắng nghe cho từng nút
        radioButtons.forEach { it.setOnClickListener(radioButtonClickListener) }

        etNumberLimit.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                updateList()
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        // Thiết lập trạng thái ban đầu
        findViewById<RadioButton>(R.id.rbOdd).isChecked = true
        updateList()
    }

    private fun updateList() {
        val limitStr = etNumberLimit.text.toString()
        val limit = if (limitStr.isNotEmpty()) limitStr.toInt() else 0

        val numbers = when (selectedRadioButtonId) {
            R.id.rbPrime -> findPrimes(limit)
            R.id.rbPerfect -> findPerfects(limit)
            R.id.rbSquare -> findSquares(limit)
            R.id.rbFibonacci -> findFibonacci(limit)
            R.id.rbEven -> findEvens(limit)
            R.id.rbOdd -> findOdds(limit)
            else -> emptyList()
        }

        displayResults(numbers)
    }

    private fun displayResults(numbers: List<Int>) {
        if (numbers.isEmpty()) {
            lvResults.visibility = View.GONE
            tvNoResults.visibility = View.VISIBLE
        } else {
            lvResults.visibility = View.VISIBLE
            tvNoResults.visibility = View.GONE
            adapter.clear()
            adapter.addAll(numbers)
            adapter.notifyDataSetChanged()
        }
    }

    // --- CÁC HÀM THUẬT TOÁN (giữ nguyên không đổi) ---
    private fun isPrime(n: Int): Boolean {
        if (n < 2) return false
        for (i in 2..sqrt(n.toDouble()).toInt()) {
            if (n % i == 0) return false
        }
        return true
    }

    private fun findPrimes(limit: Int): List<Int> = (2 until limit).filter { isPrime(it) }

    private fun findPerfects(limit: Int): List<Int> {
        val perfects = mutableListOf<Int>()
        for (n in 2 until limit) {
            var sum = 1
            for (i in 2..sqrt(n.toDouble()).toInt()) {
                if (n % i == 0) {
                    sum += i
                    if (i * i != n) sum += n / i
                }
            }
            if (sum == n) perfects.add(n)
        }
        return perfects
    }

    private fun findSquares(limit: Int): List<Int> {
        val squares = mutableListOf<Int>()
        var i = 1
        while (i * i < limit) {
            squares.add(i * i)
            i++
        }
        return squares
    }

    private fun findFibonacci(limit: Int): List<Int> {
        if (limit <= 0) return emptyList()
        val fibs = mutableListOf<Int>()
        var a = 0
        var b = 1
        while (a < limit) {
            fibs.add(a)
            val next = a + b
            a = b
            b = next
        }
        return fibs
    }

    private fun findEvens(limit: Int): List<Int> = (0 until limit).filter { it % 2 == 0 }

    private fun findOdds(limit: Int): List<Int> = (0 until limit).filter { it % 2 != 0 }
}