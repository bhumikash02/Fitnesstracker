package com.example.fitnesstracker.data

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.example.fitnesstracker.model.UserProfile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore(name = "user_prefs")

class UserPreferences(private val context: Context) {

    companion object {

        val WEIGHT = floatPreferencesKey("weight")
        val HEIGHT = floatPreferencesKey("height")
        val GENDER = stringPreferencesKey("gender")
        val IS_DARK_THEME = booleanPreferencesKey("is_dark_theme")
        val PROFILE_IMAGE_URI = stringPreferencesKey("profile_image_uri")
    }

    suspend fun saveUserProfile(profile: UserProfile) {
        context.dataStore.edit { prefs ->
            prefs[WEIGHT] = profile.weightKg
            prefs[HEIGHT] = profile.heightCm
            prefs[GENDER] = profile.gender
        }
    }

    fun getUserProfile(): Flow<UserProfile> {
        return context.dataStore.data.map { prefs ->
            UserProfile(
                weightKg = prefs[WEIGHT] ?: 60f,
                heightCm = prefs[HEIGHT] ?: 165f,
                gender = prefs[GENDER] ?: "female"
            )
        }
    }

    val themePreference: Flow<Boolean> = context.dataStore.data.map { prefs ->
        prefs[IS_DARK_THEME] ?: false // Default to light theme
    }

    suspend fun saveThemePreference(isDarkTheme: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[IS_DARK_THEME] = isDarkTheme
        }
    }

    val profileImageUri: Flow<String?> = context.dataStore.data.map { prefs ->
        prefs[PROFILE_IMAGE_URI]
    }

    suspend fun saveProfileImageUri(uri: String) {
        context.dataStore.edit { prefs ->
            prefs[PROFILE_IMAGE_URI] = uri
        }
    }
}