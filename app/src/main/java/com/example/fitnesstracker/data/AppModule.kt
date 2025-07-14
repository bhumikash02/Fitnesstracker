package com.example.fitnesstracker.data

import android.content.Context
import androidx.room.Room

object AppModule {
    fun provideDatabase(context: Context): FitnessDatabase {
        return Room.databaseBuilder(
            context,
            FitnessDatabase::class.java,
            "fitness_database"
        )
            // Add this line to handle the version change.
            // This will clear all existing data in the app.
            .fallbackToDestructiveMigration()
            .build()
    }
}