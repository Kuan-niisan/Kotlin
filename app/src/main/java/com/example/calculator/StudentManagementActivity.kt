package com.example.calculator

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class StudentManagementActivity : AppCompatActivity() {

    private lateinit var etStudentId: EditText
    private lateinit var etStudentName: EditText
    private lateinit var btnAdd: Button
    private lateinit var btnUpdate: Button
    private lateinit var rvStudents: RecyclerView

    private val studentList = mutableListOf<Student>()
    private lateinit var adapter: StudentAdapter
    private var selectedStudentPosition: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_management)

        etStudentId = findViewById(R.id.etStudentId)
        etStudentName = findViewById(R.id.etStudentName)
        btnAdd = findViewById(R.id.btnAdd)
        btnUpdate = findViewById(R.id.btnUpdate)
        rvStudents = findViewById(R.id.rvStudents)

        adapter = StudentAdapter(
            studentList,
            onItemClick = { position ->
                selectedStudentPosition = position
                val student = studentList[position]
                etStudentId.setText(student.id)
                etStudentName.setText(student.name)
                etStudentId.isEnabled = false // Không cho sửa MSSV khi đã chọn
                Toast.makeText(this, "Đã chọn: ${student.name}", Toast.LENGTH_SHORT).show()
            },
            onDeleteClick = { position ->
                studentList.removeAt(position)
                adapter.notifyItemRemoved(position)
                adapter.notifyItemRangeChanged(position, studentList.size) // Cập nhật lại vị trí
                Toast.makeText(this, "Đã xóa sinh viên", Toast.LENGTH_SHORT).show()
            }
        )

        rvStudents.layoutManager = LinearLayoutManager(this)
        rvStudents.adapter = adapter

        // Thêm một vài dữ liệu mẫu
        addSampleData()

        btnAdd.setOnClickListener {
            val id = etStudentId.text.toString().trim()
            val name = etStudentName.text.toString().trim()
            if (id.isNotEmpty() && name.isNotEmpty()) {
                studentList.add(Student(id, name))
                adapter.notifyItemInserted(studentList.size - 1)
                rvStudents.scrollToPosition(studentList.size - 1)
                clearInputs()
            } else {
                Toast.makeText(this, "Vui lòng nhập đủ thông tin", Toast.LENGTH_SHORT).show()
            }
        }

        btnUpdate.setOnClickListener {
            val newName = etStudentName.text.toString().trim()
            if (selectedStudentPosition != -1 && newName.isNotEmpty()) {
                studentList[selectedStudentPosition].name = newName
                adapter.notifyItemChanged(selectedStudentPosition)
                Toast.makeText(this, "Đã cập nhật", Toast.LENGTH_SHORT).show()
                clearInputs()
            } else {
                Toast.makeText(this, "Vui lòng chọn một sinh viên để cập nhật", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun clearInputs() {
        etStudentId.text.clear()
        etStudentName.text.clear()
        etStudentId.isEnabled = true
        selectedStudentPosition = -1
        etStudentId.requestFocus()
    }

    private fun addSampleData() {
        studentList.add(Student("20200001", "Nguyễn Văn A"))
        studentList.add(Student("20200002", "Trần Thị B"))
        studentList.add(Student("20200003", "Lê Văn C"))
        adapter.notifyDataSetChanged()
    }
}