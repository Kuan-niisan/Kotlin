package com.example.calculator

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout

class PlayStoreCloneActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play_store_clone)

        val rvOuter: RecyclerView = findViewById(R.id.rvOuter)
        val tabLayout: TabLayout = findViewById(R.id.tab_layout)

        // Setup Tabs
        tabLayout.addTab(tabLayout.newTab().setText("For you"))
        tabLayout.addTab(tabLayout.newTab().setText("Top charts"))
        tabLayout.addTab(tabLayout.newTab().setText("Other devices"))
        tabLayout.addTab(tabLayout.newTab().setText("Kids"))

        // Setup RecyclerView
        rvOuter.layoutManager = LinearLayoutManager(this)
        rvOuter.adapter = PlayStoreAdapter(generateSampleData())
    }

    private fun generateSampleData(): List<PlayStoreItem> {
        val items = mutableListOf<PlayStoreItem>()

        // Mục Sponsored
        items.add(PlayStoreItem.Header("Sponsored • Suggested for you", showArrow = false))
        items.add(PlayStoreItem.AppRow(AppItem(R.drawable.ic_app_placeholder, "Mech Assemble: Zombie Swarm", "Action • Role Playing", 4.8f, "624 MB")))
        items.add(PlayStoreItem.AppRow(AppItem(R.drawable.ic_app_placeholder, "MU: Hồng Hoà Đao", "Role Playing", 4.8f, "339 MB")))
        items.add(PlayStoreItem.AppRow(AppItem(R.drawable.ic_app_placeholder, "War Inc: Rising", "Strategy • Tower defense", 4.9f, "231 MB")))

        // Mục Recommended
        items.add(PlayStoreItem.Header("Recommended for you", showArrow = true))
        val recommendedApps = listOf(
            AppItem(R.drawable.ic_app_placeholder, "Suno", "", 0f, ""),
            AppItem(R.drawable.ic_app_placeholder, "Claude", "", 0f, ""),
            AppItem(R.drawable.ic_app_placeholder, "DramaBox", "", 0f, ""),
            AppItem(R.drawable.ic_app_placeholder, "Pika", "", 0f, "")
        )
        val recommendedCategory = AppCategory("", recommendedApps, AppCategory.TYPE_RECOMMENDED)
        items.add(PlayStoreItem.HorizontalList(recommendedCategory))

        return items
    }
}