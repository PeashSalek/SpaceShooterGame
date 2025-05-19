package com.example.produceapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.produceapp.adapter.ProduceAdapter
import com.example.produceapp.model.ProduceItem

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var produceAdapter: ProduceAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize RecyclerView - Make sure this ID matches the one in activity_main.xml
        recyclerView = findViewById(R.id.recyclerViewProduce)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Create sample data
        val produceItems = listOf(
            ProduceItem(
                1,
                "Peach",
                "Sweet and juicy summer fruit",
                "$2.99",
                R.drawable.peach
            ),
            ProduceItem(
                2,
                "Tomato",
                "Fresh vine-ripened tomato",
                "$1.49",
                R.drawable.tomato
            ),
            ProduceItem(
                3,
                "Squash",
                "Locally grown yellow squash",
                "$1.99",
                R.drawable.squash
            )
        )

        // Set up adapter with click listener
        produceAdapter = ProduceAdapter(produceItems) { selectedItem ->
            // Show detail fragment/dialog when item is clicked
            showItemDetail(selectedItem)
        }

        recyclerView.adapter = produceAdapter
    }

    private fun showItemDetail(item: ProduceItem) {
        val detailFragment = ProduceDetailFragment.newInstance(
            item.name,
            item.description,
            item.price,
            item.imageResId
        )

        detailFragment.show(supportFragmentManager, "ProduceDetail")
    }
}