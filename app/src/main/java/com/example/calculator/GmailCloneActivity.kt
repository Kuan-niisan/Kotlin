package com.example.calculator

import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlin.random.Random

class GmailCloneActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gmail_clone)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_menu_hamburger)

        val rvEmails: RecyclerView = findViewById(R.id.rvEmails)
        val fabCompose: FloatingActionButton = findViewById(R.id.fabCompose)

        val emailList = generateSampleEmails()
        val adapter = EmailAdapter(emailList)

        rvEmails.layoutManager = LinearLayoutManager(this)
        rvEmails.adapter = adapter
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.gmail_toolbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                true
            }
            R.id.action_search -> {
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun generateSampleEmails(): MutableList<Email> {
        val list = mutableListOf<Email>()
        val senders = listOf("Edurila.com", "Chris Abad", "Tuto.com", "support", "Matt from Ionic", "Google", "GitHub")
        val subjects = listOf("Bestselling Course Is Here!", "Help make Campaign Monitor better", "Formation gratuite et les...", "suivi de vos services...", "The New Ionic Creator Is Here!", "Security Alert", "New sign-in")
        val times = listOf("12:34 PM", "11:22 AM", "11:04 AM", "10:26 AM", "10:20 AM", "9:58 AM", "9:45 AM")

        for (i in 0..15) {
            val sender = senders.random()
            val randomColor = Color.argb(255, Random.nextInt(256), Random.nextInt(256), Random.nextInt(256))

            list.add(Email(
                sender = sender,
                subject = subjects.random(),
                preview = "Lorem ipsum dolor sit amet, consectetur adipiscing elit...",
                time = times.random(),
                senderInitial = sender[0],
                avatarColor = randomColor
            ))
        }
        return list
    }
}