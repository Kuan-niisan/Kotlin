package com.example.calculator

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class AddStudentActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_student)

        val etStudentId: EditText = findViewById(R.id.etStudentId)
        val etStudentName: EditText = findViewById(R.id.etStudentName)
        val etPhoneNumber: EditText = findViewById(R.id.etPhoneNumber)
        val etAddress: EditText = findViewById(R.id.etAddress)
        val btnSave: Button = findViewById(R.id.btnSave)

        btnSave.setOnClickListener {
            val id = etStudentId.text.toString().trim()
            val name = etStudentName.text.toString().trim()
            val phone = etPhoneNumber.text.toString().trim()
            val address = etAddress.text.toString().trim()

            if (id.isNotEmpty() && name.isNotEmpty() && phone.isNotEmpty() && address.isNotEmpty()) {
                val newStudent = Student(id, name, phone, address)
                val resultIntent = Intent()
                resultIntent.putExtra("NEW_STUDENT", newStudent)
                setResult(Activity.RESULT_OK, resultIntent)
                finish() // Đóng Activity và quay về
            } else {
                Toast.makeText(this, "Vui lòng nhập đủ thông tin", Toast.LENGTH_SHORT).show()
            }
        }
    }
}