package com.example.kulubs.model

data class WarungResult(
    val id: String,
    val name: String,
    val rating: Float,
    val likes: Int,
    val location: String,
    val categories: List<String>,
    val imageResId: Int,
    val phoneNumber: String
)