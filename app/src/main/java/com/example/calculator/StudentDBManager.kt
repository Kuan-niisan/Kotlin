package com.example.calculator

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase

class StudentDBManager(private val context: Context) {

    // Using the path consistent with the MainActivity provided in the prompt
    private val dbPath = context.filesDir.path + "/students.db"
    private val tableName = "tblStudent"

    val db: SQLiteDatabase = SQLiteDatabase.openDatabase(dbPath, null, SQLiteDatabase.CREATE_IF_NECESSARY)

    init {
        createTable()
        populateInitialData()
    }

    private fun createTable() {

        db.execSQL("drop table if exists $tableName")
        val sql = """
            create table if not exists $tableName(
                studentID text PRIMARY KEY,
                name text,
                phone text,
                address text
            );
        """.trimIndent()
        db.execSQL(sql)
    }

    private fun populateInitialData() {
        val cursor = db.rawQuery("select count(*) from $tableName", null)
        if (cursor.moveToFirst()) {
            val count = cursor.getInt(0)
            if (count == 0) {
                // Insert dummy data
                addStudentInternal(Student("20221010" , "John Doe", "555-1234", "123 Main St"))
                addStudentInternal(Student("20221011", "Jane Smith", "555-5678", "456 Elm Ave"))
                addStudentInternal(Student("20221012", "Alice Brown", "555-9012", "789 Oak Blvd"))
            }
        }
        cursor.close()
    }

    fun getAllStudents(): MutableList<Student> {
        val list = mutableListOf<Student>()
        val cursor: Cursor = db.rawQuery("select * from $tableName", null)

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getString(0)
                val name = cursor.getString(1)
                val phone = cursor.getString(2)
                val address = cursor.getString(3)

                list.add(Student(id, name, phone, address))
            } while (cursor.moveToNext())
        }
        cursor.close()
        return list
    }

    private fun addStudentInternal(student: Student) {
        val values = ContentValues()
        values.put("studentID", student.id)
        values.put("name", student.name)
        values.put("phone", student.phoneNumber)
        values.put("address", student.address)
        db.insert(tableName, null, values)
    }

    fun addStudent(student: Student) {
        addStudentInternal(student)
    }

    fun updateStudent(student: Student) {
        val values = ContentValues()
        values.put("name", student.name)
        values.put("phone", student.phoneNumber)
        values.put("address", student.address)

        val whereClause = "studentID = ?"
        val whereArgs = arrayOf(student.id)

        db.update(tableName, values, whereClause, whereArgs)
    }

    fun deleteStudent(id: String) {
        val whereClause = "studentID = ?"
        val whereArgs = arrayOf(id)
        db.delete(tableName, whereClause, whereArgs)
    }
}