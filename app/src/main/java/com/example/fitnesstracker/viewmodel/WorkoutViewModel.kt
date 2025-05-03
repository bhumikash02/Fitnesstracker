package com.example.fitnesstracker.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fitnesstracker.data.WorkoutRepository
import com.example.fitnesstracker.model.WorkoutEntity
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class WorkoutViewModel(private val repository: WorkoutRepository) : ViewModel() {

    // ✅ Expose all workouts using StateFlow
    val allWorkouts: StateFlow<List<WorkoutEntity>> = repository
        .getAllWorkouts()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = emptyList()
        )

    // ✅ Add workout
    fun addWorkout(name: String, sets: Int, reps: Int, duration: Int) {
        val workout = WorkoutEntity(
            exerciseName = name,
            sets = sets,
            reps = reps,
            duration = duration
        )

        viewModelScope.launch {
            repository.insertWorkout(workout)
        }
    }

    // ✅ Delete workout
    fun deleteWorkout(workout: WorkoutEntity) {
        viewModelScope.launch {
            repository.deleteWorkout(workout)
        }
    }

    // ✅ Update workout
    fun updateWorkout(workout: WorkoutEntity) {
        viewModelScope.launch {
            repository.updateWorkout(workout)
        }
    }
}
