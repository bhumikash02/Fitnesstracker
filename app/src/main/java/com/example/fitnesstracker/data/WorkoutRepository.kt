package com.example.fitnesstracker.data

import com.example.fitnesstracker.model.WorkoutEntity
import kotlinx.coroutines.flow.Flow

class WorkoutRepository(private val dao: WorkoutDao) {

    suspend fun insertWorkout(workout: WorkoutEntity) {
        dao.insertWorkout(workout)
    }

    suspend fun deleteWorkout(workout: WorkoutEntity) {
        dao.deleteWorkout(workout)
    }

    suspend fun updateWorkout(workout: WorkoutEntity) {
        dao.updateWorkout(workout)
    }

    suspend fun insertStepsAsWorkout(steps: Int) {
        val workout = WorkoutEntity(
            exerciseName = "Walking (Sensor)",
            sets = 1,
            reps = steps,
            duration = 0
        )
        dao.insertWorkout(workout)
    }

    fun getAllWorkouts(): Flow<List<WorkoutEntity>> {
        return dao.getAllWorkouts()
    }
}
