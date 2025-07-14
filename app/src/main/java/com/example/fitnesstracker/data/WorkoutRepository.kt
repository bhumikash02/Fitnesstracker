package com.example.fitnesstracker.data

import com.example.fitnesstracker.model.StepEntity
import com.example.fitnesstracker.model.WorkoutEntity
import kotlinx.coroutines.flow.Flow
import java.text.SimpleDateFormat
import java.util.*

// 1. The constructor now requires both DAOs.
class WorkoutRepository(
    private val workoutDao: WorkoutDao,
    private val stepDao: StepDao
) {

    // --- WORKOUT METHODS (Unchanged) ---
    suspend fun insertWorkout(workout: WorkoutEntity) {
        workoutDao.insertWorkout(workout)
    }

    suspend fun deleteWorkout(workout: WorkoutEntity) {
        workoutDao.deleteWorkout(workout)
    }

    suspend fun updateWorkout(workout: WorkoutEntity) {
        workoutDao.updateWorkout(workout)
    }

    fun getAllWorkouts(): Flow<List<WorkoutEntity>> {
        return workoutDao.getAllWorkouts()
    }


    // --- NEW STEP METHODS ---

    /**
     * Saves or updates the step count for today in the database.
     */
    suspend fun updateTodaySteps(steps: Int) {
        // Get today's date in a standard "YYYY-MM-DD" format.
        val todayDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        val stepEntity = StepEntity(date = todayDate, steps = steps)
        stepDao.upsert(stepEntity)
    }

    /**
     * Provides a Flow of the last 7 days of step history from the database.
     */
    fun getWeeklyStepData(): Flow<List<StepEntity>> {
        return stepDao.getStepsForLastSevenDays()
    }
}