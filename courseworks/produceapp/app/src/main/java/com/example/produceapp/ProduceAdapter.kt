package com.example.produceapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.produceapp.R
import com.example.produceapp.model.ProduceItem

class ProduceAdapter(
    private val produceList: List<ProduceItem>,
    private val onItemClick: (ProduceItem) -> Unit
) : RecyclerView.Adapter<ProduceAdapter.ProduceViewHolder>() {

    class ProduceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Find views by ID - make sure these IDs match the ones in your item_produce.xml
        val containerLayout: ConstraintLayout = itemView.findViewById(R.id.containerLayout)
        val textName: TextView = itemView.findViewById(R.id.textName)
        val textDescription: TextView = itemView.findViewById(R.id.textDescription)
        val textPrice: TextView = itemView.findViewById(R.id.textPrice)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProduceViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_produce, parent, false)
        return ProduceViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProduceViewHolder, position: Int) {
        val currentItem = produceList[position]

        holder.textName.text = currentItem.name
        holder.textDescription.text = currentItem.description
        holder.textPrice.text = currentItem.price

        holder.containerLayout.setOnClickListener {
            onItemClick(currentItem)
        }
    }

    override fun getItemCount() = produceList.size
}