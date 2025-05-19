package com.example.quickapplauncher

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class SecondActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        // Get the message from the intent
        val message = intent.getStringExtra("message") ?: "No message found"

        // Display the message in the TextView
        val messageTextView = findViewById<TextView>(R.id.messageTextView)
        messageTextView.text = message
    }
}