package com.example.fitnesstracker.viewmodel
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fitnesstracker.data.UserPreferences
import com.example.fitnesstracker.data.WorkoutRepository
import com.example.fitnesstracker.model.StepData
import com.example.fitnesstracker.model.UserProfile
import com.example.fitnesstracker.utils.GoogleFitManager
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
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

    val stepGoal = 10000

    private lateinit var preferences: UserPreferences

    fun initUserPrefs(context: Context) {
        preferences = UserPreferences(context)
        viewModelScope.launch {
            preferences.getUserProfile().collect { profile ->
                _userProfile.value = profile
                _estimatedCalories.value = calculateCalories(_todaySteps.value, profile)
            }
        }
    }

    suspend fun saveUserProfile(profile: UserProfile) {
        preferences.saveUserProfile(profile)
        _userProfile.value = profile
        _estimatedCalories.value = calculateCalories(_todaySteps.value, profile)
    }

    fun loadSteps(context: Context, account: GoogleSignInAccount) {
        GoogleFitManager.readTodayStepCount(context, account) { steps ->
            _todaySteps.value = steps
            _estimatedCalories.value = calculateCalories(steps, _userProfile.value)
        }

        GoogleFitManager.readWeeklySteps(context, account) { weekly ->
            _weeklySteps.value = weekly
        }
    }

    fun syncSensorSteps(context: Context, account: GoogleSignInAccount, steps: Int) {
        GoogleFitManager.syncStepsToGoogleFit(context, account, steps)
    }

    fun syncSensorStepsToGoogleFit(context: Context, account: GoogleSignInAccount, steps: Int) {
        GoogleFitManager.syncStepsToGoogleFit(context, account, steps)
    }

    fun updateSensorSteps(steps: Int) {
        _todaySteps.value = steps
        _estimatedCalories.value = calculateCalories(steps, _userProfile.value)
    }

    private fun calculateCalories(steps: Int, profile: UserProfile): Float {
        val stepsPerKm = 1300f
        val distanceKm = steps / stepsPerKm
        val genderFactor = if (profile.gender.lowercase() == "male") 0.75f else 0.57f
        return profile.weightKg * distanceKm * genderFactor
    }
}
