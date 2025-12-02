package com.example.calculator

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class StudentDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_detail)

        val etStudentId: EditText = findViewById(R.id.etStudentId)
        val etStudentName: EditText = findViewById(R.id.etStudentName)
        val etPhoneNumber: EditText = findViewById(R.id.etPhoneNumber)
        val etAddress: EditText = findViewById(R.id.etAddress)
        val btnUpdate: Button = findViewById(R.id.btnUpdate)

        // Nhận dữ liệu được gửi từ StudentListActivity
        val student = intent.getParcelableExtra<Student>("STUDENT_DATA")
        val position = intent.getIntExtra("STUDENT_POSITION", -1)

        if (student == null || position == -1) {
            Toast.makeText(this, "Lỗi: Không tìm thấy dữ liệu sinh viên", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // Hiển thị thông tin lên các EditText
        etStudentId.setText(student.id)
        etStudentName.setText(student.name)
        etPhoneNumber.setText(student.phoneNumber)
        etAddress.setText(student.address)

        btnUpdate.setOnClickListener {
            val newName = etStudentName.text.toString().trim()
            val newPhone = etPhoneNumber.text.toString().trim()
            val newAddress = etAddress.text.toString().trim()

            if (newName.isNotEmpty() && newPhone.isNotEmpty() && newAddress.isNotEmpty()) {
                val updatedStudent = student.copy(name = newName, phoneNumber = newPhone, address = newAddress)
                val resultIntent = Intent()
                resultIntent.putExtra("UPDATED_STUDENT", updatedStudent)
                resultIntent.putExtra("STUDENT_POSITION", position)
                setResult(Activity.RESULT_OK, resultIntent)
                finish()
            } else {
                Toast.makeText(this, "Vui lòng không để trống thông tin", Toast.LENGTH_SHORT).show()
            }
        }
    }
}