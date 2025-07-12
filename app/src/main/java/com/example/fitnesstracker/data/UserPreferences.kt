package com.example.fitnesstracker.data
import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.example.fitnesstracker.model.UserProfile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import androidx.datastore.preferences.core.Preferences

// Extension property on Context for DataStore
val Context.dataStore by preferencesDataStore(name = "user_prefs")

class UserPreferences(private val context: Context) {

    companion object {
        val WEIGHT = floatPreferencesKey("weight")
        val HEIGHT = floatPreferencesKey("height")
        val GENDER = stringPreferencesKey("gender")
    }

    // Save user profile data to DataStore
    suspend fun saveUserProfile(profile: UserProfile) {
        context.dataStore.edit { prefs ->
            prefs[WEIGHT] = profile.weightKg
            prefs[HEIGHT] = profile.heightCm
            prefs[GENDER] = profile.gender
        }
    }

    // Retrieve user profile from DataStore
    fun getUserProfile(): Flow<UserProfile> {
        return context.dataStore.data.map { prefs: Preferences ->
            UserProfile(
                weightKg = prefs[WEIGHT] ?: 60f,
                heightCm = prefs[HEIGHT] ?: 165f,
                gender = prefs[GENDER] ?: "female"
            )
        }
    }
}
