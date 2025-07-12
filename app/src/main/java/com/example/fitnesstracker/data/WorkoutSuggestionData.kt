package com.example.fitnesstracker.model

data class WorkoutSuggestion1(
    val name: String,
    val category: String,
    val videoUrl: String
)

val workoutSuggestion1 = listOf(
    // Cardio
    WorkoutSuggestion(
        name = "Jumping Jacks",
        category = "Cardio",
        videoUrl = "https://www.youtube.com/watch?v=c4DAnQ6DtF8"
    ),
    WorkoutSuggestion(
        name = "High Knees",
        category = "Cardio",
        videoUrl = "https://www.youtube.com/watch?v=OAJ_J3EZkdY"
    ),
    WorkoutSuggestion(
        name = "Mountain Climbers",
        category = "Cardio",
        videoUrl = "https://www.youtube.com/watch?v=nmwgirgXLYM"
    ),

    // Strength
    WorkoutSuggestion(
        name = "Push Ups",
        category = "Strength",
        videoUrl = "https://www.youtube.com/watch?v=_l3ySVKYVJ8"
    ),
    WorkoutSuggestion(
        name = "Squats",
        category = "Strength",
        videoUrl = "https://www.youtube.com/watch?v=aclHkVaku9U"
    ),
    WorkoutSuggestion(
        name = "Lunges",
        category = "Strength",
        videoUrl = "https://www.youtube.com/watch?v=QOVaHwm-Q6U"
    ),

    // Yoga
    WorkoutSuggestion(
        name = "Sun Salutation",
        category = "Yoga",
        videoUrl = "https://www.youtube.com/watch?v=6IUyY9Dyr5w"
    ),
    WorkoutSuggestion(
        name = "Child’s Pose",
        category = "Yoga",
        videoUrl = "https://www.youtube.com/watch?v=U4s4mEQ5VqU"
    ),
    WorkoutSuggestion(
        name = "Downward Dog",
        category = "Yoga",
        videoUrl = "https://www.youtube.com/watch?v=1VkoG1UjL8k"
    )
)
