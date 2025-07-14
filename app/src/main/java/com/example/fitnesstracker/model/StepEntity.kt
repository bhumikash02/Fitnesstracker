package com.example.fitnesstracker.model
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "daily_steps")
data class StepEntity(
    @PrimaryKey
    val date: String,
    val steps: Int
)