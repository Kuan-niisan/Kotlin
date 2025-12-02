package com.example.calculator

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.btn_bai1).setOnClickListener {
            startActivity(Intent(this, StudentListActivity::class.java))
        }
        findViewById<Button>(R.id.btn_bai2).setOnClickListener {
            startActivity(Intent(this, GmailCloneActivity::class.java))
        }
        findViewById<Button>(R.id.btn_bai3).setOnClickListener {
            startActivity(Intent(this, PlayStoreCloneActivity::class.java))
        }
    }
}