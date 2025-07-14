package com.example.fitnesstracker.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fitnesstracker.data.UserPreferences
import com.example.fitnesstracker.data.WorkoutRepository
import com.example.fitnesstracker.model.StepData
import com.example.fitnesstracker.model.StepEntity
import com.example.fitnesstracker.model.UserProfile
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import java.util.Calendar

class StepViewModel(
    private val repository: WorkoutRepository
) : ViewModel() {

    private val _todaySteps = MutableStateFlow(0)
    val todaySteps: StateFlow<Int> = _todaySteps

    private val _weeklySteps = MutableStateFlow<List<StepData>>(emptyList())
    val weeklySteps: StateFlow<List<StepData>> = _weeklySteps

    private val _userProfile = MutableStateFlow(UserProfile())
    val userProfile: StateFlow<UserProfile> = _userProfile

    val stepGoal = 500 // The fixed goal for our chart

    private lateinit var preferences: UserPreferences
    private var initialStepsFromSensor = -1

    init {
        viewModelScope.launch {
            repository.getWeeklyStepData().collect { stepEntities ->
                // When the database changes, transform the data and update the UI state.
                _weeklySteps.value = generate7DayChartData(stepEntities)
            }
        }
    }

    fun initUserPrefs(context: Context) {
        preferences = UserPreferences(context)
        viewModelScope.launch {
            preferences.getUserProfile().collect { profile ->
                _userProfile.value = profile
            }
        }
    }

    fun onSensorStepsChanged(totalSensorSteps: Int) {
        if (initialStepsFromSensor == -1) {
            initialStepsFromSensor = totalSensorSteps
        }
        val stepsTakenThisSession = totalSensorSteps - initialStepsFromSensor
        _todaySteps.value = stepsTakenThisSession

        viewModelScope.launch {
            repository.updateTodaySteps(stepsTakenThisSession)
        }
    }

    // ⭐️ FIX: This is the new, more robust function to build the chart data.
    /**
     * Generates a complete 7-day list for the weekly chart, ensuring all days of the
     * week are present, filling in 0 for days without data.
     */
    private fun generate7DayChartData(entities: List<StepEntity>): List<StepData> {
        val calendar = Calendar.getInstance()
        val dayFormat = SimpleDateFormat("EEE", Locale.getDefault()) // "Mon", "Tue", etc.
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        // Create a lookup map from the database entities for fast access (Date -> Steps)
        val stepsByDate = entities.associateBy({ it.date }, { it.steps })

        val chartData = mutableListOf<StepData>()

        // Start from 6 days ago to today to build a 7-day list.
        for (i in 6 downTo 0) {
            calendar.time = Date() // Reset to today
            calendar.add(Calendar.DAY_OF_YEAR, -i) // Go back i days
            val date = calendar.time

            val dateString = dateFormat.format(date)
            val dayAbbreviation = dayFormat.format(date)

            // Get steps from our map, or default to 0 if no entry exists for that date.
            val steps = stepsByDate[dateString] ?: 0

            chartData.add(StepData(day = dayAbbreviation, steps = steps))
        }

        return chartData
    }

    suspend fun saveUserProfile(profile: UserProfile) {
        preferences.saveUserProfile(profile)
    }
}