package com.example.calculator

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class StudentListActivity : AppCompatActivity() {

    private lateinit var rvStudents: RecyclerView
    private val studentList = mutableListOf<Student>()
    private lateinit var adapter: StudentAdapter

    // Launcher để nhận kết quả từ AddStudentActivity
    private val addStudentLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val newStudent = result.data?.getParcelableExtra<Student>("NEW_STUDENT")
            if (newStudent != null) {
                studentList.add(newStudent)
                adapter.notifyItemInserted(studentList.size - 1)
                Toast.makeText(this, "Đã thêm sinh viên mới", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Launcher để nhận kết quả từ StudentDetailActivity
    private val updateStudentLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val updatedStudent = result.data?.getParcelableExtra<Student>("UPDATED_STUDENT")
            val position = result.data?.getIntExtra("STUDENT_POSITION", -1) ?: -1
            if (updatedStudent != null && position != -1) {
                studentList[position] = updatedStudent
                adapter.notifyItemChanged(position)
                Toast.makeText(this, "Đã cập nhật thông tin", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_list)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        rvStudents = findViewById(R.id.rvStudents)

        // --- YÊU CẦU: KHI NHẤN VÀO SINH VIÊN ---
        adapter = StudentAdapter(
            studentList,
            onItemClick = { position ->
                val intent = Intent(this, StudentDetailActivity::class.java).apply {
                    putExtra("STUDENT_DATA", studentList[position])
                    putExtra("STUDENT_POSITION", position)
                }
                updateStudentLauncher.launch(intent)
            },
            onDeleteClick = { position ->
                studentList.removeAt(position)
                adapter.notifyItemRemoved(position)
                adapter.notifyItemRangeChanged(position, studentList.size)
                Toast.makeText(this, "Đã xóa sinh viên", Toast.LENGTH_SHORT).show()
            }
        )

        rvStudents.layoutManager = LinearLayoutManager(this)
        rvStudents.adapter = adapter

        addSampleData()
    }

    // --- YÊU CẦU: TẠO OPTION MENU ---
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.student_list_menu, menu)
        return true
    }

    // --- YÊU CẦU: MỞ ACTIVITY THÊM SINH VIÊN ---
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_add_student -> {
                val intent = Intent(this, AddStudentActivity::class.java)
                addStudentLauncher.launch(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun addSampleData() {
        if (studentList.isEmpty()) {
            studentList.add(Student("20200001", "Nguyễn Văn A", "0901234567", "123 Đường ABC, Q1, TPHCM"))
            studentList.add(Student("20200002", "Trần Thị B", "0907654321", "456 Đường XYZ, Q3, TPHCM"))
            adapter.notifyDataSetChanged()
        }
    }
}