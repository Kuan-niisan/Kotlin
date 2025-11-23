package com.example.calculator

sealed class PlayStoreItem {
    data class Header(val title: String, val showArrow: Boolean) : PlayStoreItem()
    data class AppRow(val app: AppItem) : PlayStoreItem()
    data class HorizontalList(val category: AppCategory) : PlayStoreItem()
}