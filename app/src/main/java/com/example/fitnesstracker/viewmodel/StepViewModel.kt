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

class StepViewModel(
    private val repository: WorkoutRepository
) : ViewModel() {


    private val _todaySteps = MutableStateFlow(0)
    val todaySteps: StateFlow<Int> = _todaySteps
    private val _weeklySteps = MutableStateFlow<List<StepData>>(emptyList())
    val weeklySteps: StateFlow<List<StepData>> = _weeklySteps
    private val _userProfile = MutableStateFlow(UserProfile())
    val userProfile: StateFlow<UserProfile> = _userProfile
    val stepGoal = 10000
    private var initialStepsFromSensor = -1
    private val _isDarkTheme = MutableStateFlow(false)
    val isDarkTheme: StateFlow<Boolean> = _isDarkTheme
    private val _profileImageUri = MutableStateFlow<String?>(null)
    val profileImageUri: StateFlow<String?> = _profileImageUri

    private lateinit var preferences: UserPreferences

    init {
        viewModelScope.launch {
            repository.getWeeklyStepData().collect { stepEntities ->
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
        viewModelScope.launch {
            preferences.themePreference.collect { isDark ->
                _isDarkTheme.value = isDark
            }
        }
        viewModelScope.launch {
            preferences.profileImageUri.collect { uri ->
                _profileImageUri.value = uri
            }
        }
    }

    fun onSensorStepsChanged(totalSensorSteps: Int) {
        if (initialStepsFromSensor == -1) initialStepsFromSensor = totalSensorSteps
        val stepsTakenThisSession = totalSensorSteps - initialStepsFromSensor
        _todaySteps.value = stepsTakenThisSession
        viewModelScope.launch {
            repository.updateTodaySteps(stepsTakenThisSession)
        }
    }

    fun toggleTheme() {
        viewModelScope.launch {
            val newThemeState = !_isDarkTheme.value
            preferences.saveThemePreference(newThemeState)
        }
    }

    fun saveProfileImage(uri: String) {
        viewModelScope.launch {
            preferences.saveProfileImageUri(uri)
        }
    }

    private fun generate7DayChartData(entities: List<StepEntity>): List<StepData> {
        val calendar = Calendar.getInstance()
        val dayFormat = SimpleDateFormat("EEE", Locale.getDefault())
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val stepsByDate = entities.associateBy({ it.date }, { it.steps })
        val chartData = mutableListOf<StepData>()
        for (i in 6 downTo 0) {
            calendar.time = Date()
            calendar.add(Calendar.DAY_OF_YEAR, -i)
            val date = calendar.time
            val dateString = dateFormat.format(date)
            val dayAbbreviation = dayFormat.format(date)
            val steps = stepsByDate[dateString] ?: 0
            chartData.add(StepData(day = dayAbbreviation, steps = steps))
        }
        return chartData
    }

    suspend fun saveUserProfile(profile: UserProfile) {
        preferences.saveUserProfile(profile)
    }
}