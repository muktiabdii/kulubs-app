package com.example.kulubs.ui.activity

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import android.widget.*
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.kulubs.R
import com.example.kulubs.model.Review

class AddReviewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_tambah_review)

        val ratingBar = findViewById<RatingBar>(R.id.ratingBar)
        val etReview = findViewById<EditText>(R.id.et_deskripsi)
        val wordCountTextView = findViewById<TextView>(R.id.tv_WordCount)
        val button = findViewById<Button>(R.id.button)
        val btnHapus = findViewById<TextView>(R.id.btnHapus)


        btnHapus.visibility = View.GONE

        etReview.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val wordCount = countWords(s.toString())
                wordCountTextView.text = "$wordCount/500"

                btnHapus.visibility = if (wordCount > 0) View.VISIBLE else View.GONE
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        btnHapus.setOnClickListener {
            etReview.text.clear()
            btnHapus.visibility = View.GONE
        }

        button.setOnClickListener {
            val reviewText = etReview.text.toString()
            val ratingValue = ratingBar.rating.toInt()
            val wordCount = countWords(reviewText)
            val username = "Pengguna Saya" // bisa diganti jadi input dari EditText
            val newReview = Review(
                reviewText = reviewText,
                rating = ratingValue,
                username = username,
                isUserReview = true
            )

            when{
                ratingBar.rating == 0f -> {
                    Toast.makeText(this, "Silakan beri rating terlebih dahulu", Toast.LENGTH_SHORT).show()
                }
                reviewText.isEmpty() -> {
                    Toast.makeText(this, "Review tidak boleh kosong", Toast.LENGTH_SHORT).show()
                }
                wordCount > 500 -> {
                    Toast.makeText(this, "Review tidak boleh lebih dari 500 kata", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    Toast.makeText(this, "Review berhasil ditambahkan", Toast.LENGTH_SHORT).show()

                    val intent = Intent(this, AllReviewActivity::class.java).apply {
                        putExtra("RATING_VALUE", ratingValue)
                        putExtra("REVIEW_TEXT", reviewText)
                        putExtra("USERNAME", username)
                    }

                    startActivity(intent)
                }
            }
        }
    }

    private fun countWords(text: String): Int {
        return text.trim().split("\\s+".toRegex()).filter { it.isNotEmpty() }.size
    }
}