package com.example.fitnesstracker.ui.theme.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.fitnesstracker.model.UserProfile
import com.example.fitnesstracker.ui.components.ProfileImage
import com.example.fitnesstracker.viewmodel.StepViewModel
import kotlin.math.roundToInt

@Composable
fun ProfileScreen(
    userProfile: UserProfile,
    isDarkTheme: Boolean,
    profileImageUri: String?,
    onSaveProfile: (UserProfile) -> Unit,
    onLogout: () -> Unit,
    onThemeToggle: () -> Unit,
    onSaveImage: (String) -> Unit
) {
    var name by remember(userProfile.name) { mutableStateOf(userProfile.name) }
    var age by remember(userProfile.age) { mutableStateOf(userProfile.age.toString()) }
    var height by remember(userProfile.heightCm) { mutableStateOf(userProfile.heightCm.toString()) }
    var weight by remember(userProfile.weightKg) { mutableStateOf(userProfile.weightKg.toString()) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(16.dp))
            ProfileImage(
                imageUri = profileImageUri,
                onImageUriChanged = onSaveImage
            )
            Spacer(modifier = Modifier.height(24.dp))
        }
        item {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Name") },
                modifier = Modifier.fillMaxWidth()
            )
        }
        item {
            OutlinedTextField(
                value = age,
                onValueChange = { age = it },
                label = { Text("Age") },
                modifier = Modifier.fillMaxWidth()
            )
        }
        item {
            OutlinedTextField(
                value = height,
                onValueChange = { height = it },
                label = { Text("Height (cm)") },
                modifier = Modifier.fillMaxWidth()
            )
        }
        item {
            OutlinedTextField(
                value = weight,
                onValueChange = { weight = it },
                label = { Text("Weight (kg)") },
                modifier = Modifier.fillMaxWidth()
            )
        }
        item {
            Text(
                text = "BMI: ${userProfile.bmi.roundToInt()}",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }
        item {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Dark Theme")
                Switch(
                    checked = isDarkTheme,
                    onCheckedChange = { onThemeToggle() }
                )
            }
        }
        item {
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    onSaveProfile(
                        UserProfile(
                            name = name,
                            age = age.toIntOrNull() ?: 0,
                            heightCm = height.toFloatOrNull() ?: 0f,
                            weightKg = weight.toFloatOrNull() ?: 0f,
                            gender = userProfile.gender
                        )
                    )
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save Profile")
            }
        }
        item {
            OutlinedButton(
                onClick = onLogout,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            ) {
                Text("Logout")
            }
        }
    }
}