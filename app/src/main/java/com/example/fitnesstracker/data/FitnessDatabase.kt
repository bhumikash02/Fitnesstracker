package com.example.fitnesstracker.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.fitnesstracker.model.WorkoutEntity

@Database(entities = [WorkoutEntity::class], version = 1)
abstract class FitnessDatabase : RoomDatabase() {
    abstract fun workoutDao(): WorkoutDao
}
