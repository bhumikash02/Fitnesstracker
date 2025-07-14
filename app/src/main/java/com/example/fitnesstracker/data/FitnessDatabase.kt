package com.example.fitnesstracker.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.fitnesstracker.model.StepEntity
import com.example.fitnesstracker.model.WorkoutEntity

@Database(entities = [WorkoutEntity::class, StepEntity::class], version = 2)
abstract class FitnessDatabase : RoomDatabase() {
    abstract fun workoutDao(): WorkoutDao

    // 3. Add the new StepDao abstract function
    abstract fun stepDao(): StepDao
}