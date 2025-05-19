package com.example.coursework

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val firstNumberEditText: EditText = findViewById(R.id.firstNumberEditText)
        val secondNumberEditText: EditText = findViewById(R.id.secondNumberEditText)
        val addButton: Button = findViewById(R.id.addButton)
        val resultTextView: TextView = findViewById(R.id.resultTextView)

        // Show a toast just to verify the activity is loading
        Toast.makeText(this, "App started successfully", Toast.LENGTH_SHORT).show()

        addButton.setOnClickListener {
            try {
                val num1 = firstNumberEditText.text.toString().toInt()
                val num2 = secondNumberEditText.text.toString().toInt()
                val sum = num1 + num2
                resultTextView.text = sum.toString()

                // Show a toast when calculation is done
                Toast.makeText(this, "Calculation completed", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                // Show error if something goes wrong
                Toast.makeText(this, "Please enter valid numbers", Toast.LENGTH_SHORT).show()
            }
        }
    }
}