package com.example.spaceshootergame

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Instead of creating a GameActivity instance, start it using an Intent
        val intent = Intent(this, GameActivity::class.java)
        startActivity(intent)
        finish() // Optional: close the MainActivity after starting GameActivity
    }
}
