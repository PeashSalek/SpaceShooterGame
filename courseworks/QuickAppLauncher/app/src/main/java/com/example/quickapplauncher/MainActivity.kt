package com.example.quickapplauncher

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Reference the buttons
        val secondActivityBtn = findViewById<Button>(R.id.secondActivityBtn)
        val googleSearchBtn = findViewById<Button>(R.id.googleSearchBtn)

        // Set up click listener for the second activity button
        secondActivityBtn.setOnClickListener {
            // Create an Intent to start the second activity
            val intent = Intent(this, SecondActivity::class.java)

            // Add data to pass to the second activity
            intent.putExtra("message", "Hello World")

            // Start the second activity
            startActivity(intent)
        }

        // Set up click listener for the Google search button
        googleSearchBtn.setOnClickListener {
            // Create an Intent to open a web page
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com"))

            try {
                startActivity(browserIntent)
            } catch (e: Exception) {
                // This will handle the case when no browser is available
                Toast.makeText(this, "No browser found on this device", Toast.LENGTH_LONG).show()
                e.printStackTrace() // Log the exception for debugging
            }
        }
    }
}