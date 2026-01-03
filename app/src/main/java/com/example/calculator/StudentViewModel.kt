package com.example.calculator

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class StudentViewModel(application: Application) : AndroidViewModel(application) {

    private val dbManager = StudentDBManager(application)

    private val _studentList = MutableLiveData<MutableList<Student>>()
    val studentList: LiveData<MutableList<Student>> = _studentList

    private val _selectedStudent = MutableLiveData<Student?>()
    val selectedStudent: LiveData<Student?> = _selectedStudent
    private var selectedStudentPosition = -1

    init {
        loadStudents()
    }

    private fun loadStudents() {
        _studentList.value = dbManager.getAllStudents()
    }

    fun addStudent(student: Student) {
        dbManager.addStudent(student)
        loadStudents()
    }

    fun updateStudent(newName: String, newPhone: String, newAddress: String) {
        if (selectedStudentPosition != -1) {
            _studentList.value?.get(selectedStudentPosition)?.let {
                val updatedStudent = it.copy(name = newName, phoneNumber = newPhone, address = newAddress)
                dbManager.updateStudent(updatedStudent)
                loadStudents()
            }
        }
    }

    fun deleteStudent(position: Int) {
        _studentList.value?.get(position)?.let {
            dbManager.deleteStudent(it.id)
            loadStudents()
        }
    }

    fun onStudentSelected(position: Int) {
        selectedStudentPosition = position
        _selectedStudent.value = _studentList.value?.get(position)
    }

    fun doneNavigating() {
        _selectedStudent.value = null
        selectedStudentPosition = -1
    }
}