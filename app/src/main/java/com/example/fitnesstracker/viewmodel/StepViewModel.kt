package com.example.fitnesstracker.viewmodel
import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fitnesstracker.data.UserPreferences
import com.example.fitnesstracker.data.WorkoutRepository
import com.example.fitnesstracker.model.StepData
import com.example.fitnesstracker.model.UserProfile
import com.example.fitnesstracker.utils.HealthConnectManager
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class StepViewModel(
    private val repository: WorkoutRepository
) : ViewModel() {

    private val _todaySteps = MutableStateFlow(0)
    val todaySteps: StateFlow<Int> = _todaySteps

    private val _weeklySteps = MutableStateFlow<List<StepData>>(emptyList())
    val weeklySteps: StateFlow<List<StepData>> = _weeklySteps

    private val _estimatedCalories = MutableStateFlow(0f)
    val estimatedCalories: StateFlow<Float> = _estimatedCalories

    private val _userProfile = MutableStateFlow(UserProfile())
    val userProfile: StateFlow<UserProfile> = _userProfile

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    val stepGoal = 10000

    private var healthConnectManager: HealthConnectManager? = null
    private lateinit var preferences: UserPreferences

    private var initialStepsFromHealthConnect = 0
    private var initialStepsFromSensor = -1 // -1 means sensor baseline is not yet set

    fun initUserPrefs(context: Context) {
        preferences = UserPreferences(context)
        viewModelScope.launch {
            preferences.getUserProfile().collect { profile ->
                _userProfile.value = profile
                recalculateCalories()
            }
        }
    }

    fun getOrCreateHealthConnectManager(context: Context): HealthConnectManager {
        if (healthConnectManager == null) {
            healthConnectManager = HealthConnectManager(context)
        }
        return healthConnectManager!!
    }

    suspend fun saveUserProfile(profile: UserProfile) {
        preferences.saveUserProfile(profile)
        _userProfile.value = profile
        recalculateCalories()
    }

    fun loadStepsFromHealthConnect() {
        healthConnectManager?.let { manager ->
            _isLoading.value = true
            viewModelScope.launch {
                // Read today's steps from Health Connect to set a baseline
                initialStepsFromHealthConnect = manager.readTodayStepCount()
                _todaySteps.value = initialStepsFromHealthConnect

                // Also load the weekly chart data
                _weeklySteps.value = manager.readWeeklySteps()
                recalculateCalories()
                _isLoading.value = false
            }
        }
    }

    // ⭐️ --- START OF THE FIX --- ⭐️
    fun onSensorStepsChanged(totalSensorSteps: Int) {
        // Log to confirm the ViewModel is receiving the event
        Log.d("StepDebug", "VIEWMODEL UPDATE: Received sensor total: $totalSensorSteps")

        // If this is the first sensor event we've received, record the value
        // as our starting point for this session.
        if (initialStepsFromSensor == -1) {
            initialStepsFromSensor = totalSensorSteps
        }

        // Calculate how many steps have been taken since the app started listening
        val stepsTakenThisSession = totalSensorSteps - initialStepsFromSensor

        // The new total is the historical data from Health Connect plus the new steps from this session.
        // We use maxOf to prevent the count from going down if the sensor resets or Health Connect syncs.
        val newTotal = maxOf(initialStepsFromHealthConnect, initialStepsFromHealthConnect + stepsTakenThisSession)

        _todaySteps.value = newTotal
        recalculateCalories()
    }
    // ⭐️ --- END OF THE FIX --- ⭐️

    private fun recalculateCalories() {
        val steps = _todaySteps.value
        val profile = _userProfile.value
        val stepsPerKm = 1300f
        val distanceKm = steps / stepsPerKm
        val genderFactor = if (profile.gender.lowercase() == "male") 0.75f else 0.57f
        _estimatedCalories.value = profile.weightKg * distanceKm * genderFactor
    }
}