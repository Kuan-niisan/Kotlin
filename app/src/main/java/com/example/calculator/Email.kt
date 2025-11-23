package com.example.calculator

data class Email(
    val sender: String,
    val subject: String,
    val preview: String,
    val time: String,
    val senderInitial: Char,
    val avatarColor: Int,
    var isStarred: Boolean = false
)
