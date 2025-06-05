package com.example.kulubs.adapter

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kulubs.R
import com.example.kulubs.model.FilterOption
import com.google.android.material.card.MaterialCardView

class FilterAdapter(
    private val filterList: List<FilterOption>,
    private val onFilterClick: (FilterOption) -> Unit
) : RecyclerView.Adapter<FilterAdapter.FilterViewHolder>() {

    inner class FilterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cardFilter: MaterialCardView = itemView.findViewById(R.id.chipCard)
        val textFilter: TextView = itemView.findViewById(R.id.chipText) // Use chipText from item_filter.xml
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilterViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_filter, parent, false)
        return FilterViewHolder(view)
    }

    override fun onBindViewHolder(holder: FilterViewHolder, position: Int) {
        val item = filterList[position]
        holder.textFilter.text = item.name
        holder.textFilter.setTextColor(
            if (item.isSelected) Color.WHITE else Color.parseColor("#FF5F00")
        )
        holder.cardFilter.setCardBackgroundColor(
            if (item.isSelected) Color.parseColor("#FF5F00") else Color.WHITE
        )
        holder.cardFilter.isChecked = item.isSelected // Use checked state for visual feedback

        holder.cardFilter.setOnClickListener {
            filterList.forEach { it.isSelected = false }
            item.isSelected = true
            notifyDataSetChanged()
            onFilterClick(item)
        }
    }

    override fun getItemCount(): Int = filterList.size
}