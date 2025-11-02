package com.example.calculator

import android.app.AlertDialog
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import java.text.SimpleDateFormat
import java.util.*

class RegisterFormActivity : AppCompatActivity() {

    private lateinit var inputFirstName: EditText
    private lateinit var inputLastName: EditText
    private lateinit var groupGender: RadioGroup
    private lateinit var inputBirthday: EditText
    private lateinit var buttonSelect: Button
    private lateinit var inputAddress: EditText
    private lateinit var inputEmail: EditText
    private lateinit var checkboxTerms: CheckBox
    private lateinit var buttonRegister: Button

    private var defaultEditTextBackground: ColorStateList? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_form)

        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayUseLogoEnabled(true)
        supportActionBar?.setLogo(R.drawable.ic_lifecycle_logo)

        inputFirstName = findViewById(R.id.input_first_name)
        inputLastName = findViewById(R.id.input_last_name)
        groupGender = findViewById(R.id.group_gender)
        inputBirthday = findViewById(R.id.input_birthday)
        buttonSelect = findViewById(R.id.button_select)
        inputAddress = findViewById(R.id.input_address)
        inputEmail = findViewById(R.id.input_email)
        checkboxTerms = findViewById(R.id.checkbox_terms)
        buttonRegister = findViewById(R.id.button_register)

        defaultEditTextBackground = inputFirstName.backgroundTintList

        buttonSelect.setOnClickListener {
            showCalendarDialog()
        }

        buttonRegister.setOnClickListener {
            if (validateInputs()) {
                Toast.makeText(this, "Registration Successful!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Please fill in all required fields.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showCalendarDialog() {
        val builder = AlertDialog.Builder(this)
        val inflater = LayoutInflater.from(this)
        val dialogView = inflater.inflate(R.layout.calendar, null)
        val calendarViewInDialog = dialogView.findViewById<CalendarView>(R.id.dialog_calendar_view)

        builder.setView(dialogView)
        val dialog = builder.create()

        calendarViewInDialog.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val calendar = Calendar.getInstance()
            calendar.set(year, month, dayOfMonth)
            val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val formattedDate = sdf.format(calendar.time)
            inputBirthday.setText(formattedDate)
            dialog.dismiss()
        }

        dialog.show()
    }
    private fun validateInputs(): Boolean {
        var isValid = true
        val errorColor = ContextCompat.getColor(this, R.color.validation_error_background)
        val errorTint = ColorStateList.valueOf(errorColor) // Tạo tint list từ màu đỏ

        if (inputFirstName.text.trim().isEmpty()) {
            inputFirstName.backgroundTintList = errorTint // Dùng tint list
            isValid = false
        } else {
            inputFirstName.backgroundTintList = defaultEditTextBackground
        }

        if (inputLastName.text.trim().isEmpty()) {
            inputLastName.backgroundTintList = errorTint // Dùng tint list
            isValid = false
        } else {
            inputLastName.backgroundTintList = defaultEditTextBackground
        }

        if (inputBirthday.text.trim().isEmpty()) {
            inputBirthday.backgroundTintList = errorTint // Dùng tint list
            isValid = false
        } else {
            inputBirthday.backgroundTintList = defaultEditTextBackground
        }

        if (inputAddress.text.trim().isEmpty()) {
            inputAddress.backgroundTintList = errorTint // Dùng tint list
            isValid = false
        } else {
            inputAddress.backgroundTintList = defaultEditTextBackground
        }

        if (inputEmail.text.trim().isEmpty()) {
            inputEmail.backgroundTintList = errorTint // Dùng tint list
            isValid = false
        } else {
            inputEmail.backgroundTintList = defaultEditTextBackground
        }

        if (groupGender.checkedRadioButtonId == -1) {
            findViewById<RadioButton>(R.id.radio_male).setTextColor(Color.RED)
            findViewById<RadioButton>(R.id.radio_female).setTextColor(Color.RED)
            isValid = false
        } else {
            findViewById<RadioButton>(R.id.radio_male).setTextColor(Color.BLACK)
            findViewById<RadioButton>(R.id.radio_female).setTextColor(Color.BLACK)
        }

        if (!checkboxTerms.isChecked) {
            checkboxTerms.setTextColor(Color.RED)
            isValid = false
        } else {
            checkboxTerms.setTextColor(Color.BLACK)
        }

        return isValid
    }
}