package com.example.fitnesstracker.model

data class UserProfile(
    val weightKg: Float = 60f,
    val heightCm: Float = 165f,
    val gender: String = "female" // "male" or "female"
)
