package com.example.kulubs.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.kulubs.R
import com.example.kulubs.databinding.ItemResultBinding
import com.example.kulubs.model.Warung

class WarungAdapter(
    private var warungList: List<Warung>,
    private val onItemClick: (Warung) -> Unit
) : RecyclerView.Adapter<WarungAdapter.WarungViewHolder>() {

    inner class WarungViewHolder(val binding: ItemResultBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WarungViewHolder {
        val binding = ItemResultBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return WarungViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WarungViewHolder, position: Int) {
        val warung = warungList[position]

        with(holder.binding) {
            imgWarung.setImageResource(warung.imageRes)
            tvWarungName.text = warung.name
            tvLocation.text = warung.location
            tvCategories.text = warung.categories

            // Set rating stars (you'll need to implement this logic)
            // Set like count
            tvLikeCount.text = "Disukai ${warung.likes}"

            root.setOnClickListener { onItemClick(warung) }

            btnWhatsapp.setOnClickListener {
                // Handle WhatsApp button click
            }

            btnReview.setOnClickListener {
                // Handle review button click
            }
        }
    }

    override fun getItemCount(): Int = warungList.size

    fun updateList(newList: List<Warung>) {
        warungList = newList
        notifyDataSetChanged()
    }
}