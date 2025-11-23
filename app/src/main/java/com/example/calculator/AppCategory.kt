package com.example.calculator

data class AppCategory(
    val title: String,
    val apps: List<AppItem>,
    val viewType: Int // Dùng để phân biệt kiểu hiển thị (Sponsored hay Recommended)
) {
    companion object {
        const val TYPE_SPONSORED = 0
        const val TYPE_RECOMMENDED = 1
    }
}