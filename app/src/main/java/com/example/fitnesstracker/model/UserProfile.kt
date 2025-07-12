package com.example.fitnesstracker.model

data class UserProfile(
    val name: String = "Bhumika Sharma ",
    val age: Int = 20,
    val heightCm: Float = 165f,
    val weightKg: Float = 67f,
    val gender: String = "Female"
) {
    val bmi: Float
        get() = if (heightCm > 0) weightKg / ((heightCm / 100) * (heightCm / 100)) else 0f
}
