package com.example.calculator

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class StudentViewModel : ViewModel() {

    private val _studentList = MutableLiveData<MutableList<Student>>()
    val studentList: LiveData<MutableList<Student>> = _studentList

    private val _selectedStudent = MutableLiveData<Student?>()
    val selectedStudent: LiveData<Student?> = _selectedStudent
    private var selectedStudentPosition = -1

    init {
        val sampleList = mutableListOf(
            Student("20200001", "Nguyễn Văn A", "0901234567", "123 Đường ABC, Q1, TPHCM"),
            Student("20200002", "Trần Thị B", "0907654321", "456 Đường XYZ, Q3, TPHCM")
        )
        _studentList.value = sampleList
    }

    fun addStudent(student: Student) {
        val list = _studentList.value ?: mutableListOf()
        list.add(student)
        _studentList.value = list
    }

    fun updateStudent(newName: String, newPhone: String, newAddress: String) {
        if (selectedStudentPosition != -1) {
            val list = _studentList.value
            list?.let {
                val studentToUpdate = it[selectedStudentPosition]
                studentToUpdate.name = newName
                studentToUpdate.phoneNumber = newPhone
                studentToUpdate.address = newAddress
                _studentList.value = it
            }
        }
    }

    fun deleteStudent(position: Int) {
        val list = _studentList.value ?: return
        list.removeAt(position)
        _studentList.value = list
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