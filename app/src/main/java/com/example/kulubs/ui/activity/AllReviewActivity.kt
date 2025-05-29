package com.example.kulubs.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kulubs.R
import com.example.kulubs.adapter.ReviewAdapter
import com.example.kulubs.model.Review

class AllReviewActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var reviewAdapter: ReviewAdapter
    private val reviewList = mutableListOf<Review>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_review)

        val btnBack = findViewById<View>(R.id.btnBack)
        btnBack.setOnClickListener {
            val intent = Intent(this, AddReviewActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish() // agar tidak menumpuk di backstack
        }

        recyclerView = findViewById(R.id.reviewRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        reviewAdapter = ReviewAdapter(reviewList) { review ->
            // Callback untuk menghapus review user
            reviewList.remove(review)
            reviewAdapter.notifyDataSetChanged()
            Toast.makeText(this, "Review dihapus", Toast.LENGTH_SHORT).show()
        }

        recyclerView.adapter = reviewAdapter

        // Ambil data dari Intent
        val userReviewText = intent.getStringExtra("REVIEW_TEXT")
        val userRating = intent.getIntExtra("RATING_VALUE", 0)
        val username = intent.getStringExtra("USERNAME") ?: "Saya"

        // Tambahkan review dari user jika ada
        if (!userReviewText.isNullOrEmpty()) {
            val userReview = Review(
                reviewText = userReviewText,
                rating = userRating,
                username = username,
                isUserReview = true
            )
            reviewList.add(userReview)
        }

        // Tambahkan review dummy pengguna lain
        reviewList.addAll(
            listOf(
                Review(reviewText = "Sumpah, ini salah satu warung makan terbaik di sekitar kampus! Aku pesan ayam geprek level 3, dan rasanya bener-bener pas. Ayamnya crispy di luar, tapi tetap juicy di dalam, dan sambalnya pedasnya bikin nagih! Nasi hangatnya juga pas banget buat nemenin ayamnya. Porsinya lumayan besar dengan harga yang masih ramah kantong mahasiswa. Ditambah lagi, pelayanan di sini cepat dan ramah. Tempatnya bersih, ada tempat duduk indoor dan outdoor, cocok buat makan rame-rame bareng teman. Bakal sering ke sini lagi!", rating = 5, username = "Andi"),
                Review(reviewText = "Sumpah, ini salah satu warung makan terbaik di sekitar kampus! Aku pesan ayam geprek level 3, dan rasanya bener-bener pas. Ayamnya crispy di luar, tapi tetap juicy di dalam, dan sambalnya pedasnya bikin nagih! Nasi hangatnya juga pas banget buat nemenin ayamnya. Porsinya lumayan besar dengan harga yang masih ramah kantong mahasiswa. Ditambah lagi, pelayanan di sini cepat dan ramah. Tempatnya bersih, ada tempat duduk indoor dan outdoor, cocok buat makan rame-rame bareng teman. Bakal sering ke sini lagi!", rating = 4, username = "Budi"),
                Review(reviewText = "Sumpah, ini salah satu warung makan terbaik di sekitar kampus! Aku pesan ayam geprek level 3, dan rasanya bener-bener pas. Ayamnya crispy di luar, tapi tetap juicy di dalam, dan sambalnya pedasnya bikin nagih! Nasi hangatnya juga pas banget buat nemenin ayamnya. Porsinya lumayan besar dengan harga yang masih ramah kantong mahasiswa. Ditambah lagi, pelayanan di sini cepat dan ramah. Tempatnya bersih, ada tempat duduk indoor dan outdoor, cocok buat makan rame-rame bareng teman. Bakal sering ke sini lagi!", rating = 3, username = "Citra")
            )
        )

        reviewAdapter.notifyDataSetChanged()
    }

}
