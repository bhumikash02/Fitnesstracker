package com.example.fitnesstracker.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "workouts")
data class WorkoutEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val exerciseName: String,
    val sets: Int,
    val reps: Int,
    val duration: Int,
    val timestamp: Long = System.currentTimeMillis()
)
