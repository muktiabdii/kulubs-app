package com.example.kulubs.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kulubs.R
import com.example.kulubs.model.Review

class ReviewAdapter(
    private val reviews: MutableList<Review>,
    private val onDeleteClick: (Review) -> Unit
) : RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder>() {

    inner class ReviewViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvReview: TextView = view.findViewById(R.id.tvItemReview)
        val ratingBar: RatingBar = view.findViewById(R.id.ratingBarItem)
        val btnDelete: TextView = view.findViewById(R.id.tvDelete)
        val tvUsername: TextView = view.findViewById(R.id.tvUsername)
        val tvLabel: TextView = view.findViewById(R.id.tvLabel)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_layout, parent, false)
        return ReviewViewHolder(view)
    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        val review = reviews[position]

        holder.tvReview.text = review.reviewText
        holder.ratingBar.rating = review.rating.toFloat()
        holder.tvUsername.text = review.username

        if (review.isUserReview) {
            holder.btnDelete.visibility = View.VISIBLE
            holder.tvLabel.visibility = View.VISIBLE
            holder.tvLabel.text = "Komentar Anda"
            holder.btnDelete.setOnClickListener {
                onDeleteClick(review)
            }
        } else {
            holder.btnDelete.visibility = View.GONE
            holder.tvLabel.visibility = View.VISIBLE
            holder.tvLabel.text = "Komentar Pengguna Lain"
        }
    }

    override fun getItemCount(): Int = reviews.size
}
