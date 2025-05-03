package com.example.fitnesstracker.model
data class Exercise(
        val name: String,
        val duration: Int, // in minutes
        val sets: Int,
        val reps: Int
    )