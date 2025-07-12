package com.example.fitnesstracker.model

data class WorkoutSuggestion(
    val name: String,
    val category: String,
    val videoUrl: String
)

val workoutSuggestions = listOf(
    WorkoutSuggestion("Push Ups", "Strength", "https://youtu.be/IODxDxX7oi4"),
    WorkoutSuggestion("Jumping Jacks", "Cardio", "https://youtu.be/2W4ZNSwoW_4"),
    WorkoutSuggestion("Plank", "Core", "https://youtu.be/pSHjTRCQxIw"),
    WorkoutSuggestion("Squats", "Strength", "https://youtu.be/aclHkVaku9U"),
    WorkoutSuggestion("Burpees", "Cardio", "https://youtu.be/dZgVxmf6jkA")
)
