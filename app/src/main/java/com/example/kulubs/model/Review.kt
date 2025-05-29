package com.example.kulubs.model

import java.util.UUID

data class Review(
    val id: String = UUID.randomUUID().toString(), // ID unik untuk setiap ulasan
    val reviewText: String,
    val rating: Int,
    val username: String,
    val isUserReview: Boolean = false // Menunjukkan apakah ulasan dibuat oleh pengguna
)