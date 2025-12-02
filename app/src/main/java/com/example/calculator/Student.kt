package com.example.calculator
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
@Parcelize
data class Student(
    val id: String,
    var name: String,
    var phoneNumber: String,
    var address: String
) : Parcelable