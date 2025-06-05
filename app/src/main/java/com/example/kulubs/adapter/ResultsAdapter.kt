package com.example.kulubs.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kulubs.R
import com.example.kulubs.model.WarungResult
import com.google.android.material.button.MaterialButton

class ResultsAdapter(
    private val resultsList: List<WarungResult>,
    private val onWhatsappClick: (WarungResult) -> Unit,
    private val onReviewClick: (WarungResult) -> Unit
) : RecyclerView.Adapter<ResultsAdapter.ResultViewHolder>() {

    inner class ResultViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgWarung: ImageView = itemView.findViewById(R.id.img_warung)
        val tvWarungName: TextView = itemView.findViewById(R.id.tv_warung_name)
        val tvRating: TextView = itemView.findViewById(R.id.tv_rating)
        val tvLikes: TextView = itemView.findViewById(R.id.tv_likes)
        val tvLocation: TextView = itemView.findViewById(R.id.tv_location)
        val tvCategories: TextView = itemView.findViewById(R.id.tv_categories)
        val btnWhatsapp: MaterialButton = itemView.findViewById(R.id.btn_whatsapp)
        val btnReview: MaterialButton = itemView.findViewById(R.id.btn_review)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_result, parent, false)
        return ResultViewHolder(view)
    }

    override fun onBindViewHolder(holder: ResultViewHolder, position: Int) {
        val item = resultsList[position]

        // Set data to views
        holder.tvWarungName.text = item.name
        holder.tvRating.text = item.rating.toString()
        holder.tvLikes.text = "Disukai ${item.likes}"
        holder.tvLocation.text = item.location
        holder.tvCategories.text = item.categories.joinToString(", ")

        // Set image
        // You may use an image loading library like Glide or Picasso here
        holder.imgWarung.setImageResource(item.imageResId)

        // Set button click listeners
        holder.btnWhatsapp.setOnClickListener {
            onWhatsappClick(item)
        }

        holder.btnReview.setOnClickListener {
            onReviewClick(item)
        }
    }

    override fun getItemCount(): Int = resultsList.size
}