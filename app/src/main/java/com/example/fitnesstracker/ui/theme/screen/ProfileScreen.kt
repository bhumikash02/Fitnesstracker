package com.example.fitnesstracker.ui.theme.screen

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.fitnesstracker.model.UserProfile
import com.example.fitnesstracker.viewmodel.StepViewModel
import kotlinx.coroutines.launch

@Composable
fun ProfileScreen(viewModel: StepViewModel) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    // Load user data on screen entry
    LaunchedEffect(Unit) {
        viewModel.initUserPrefs(context)
    }

    val profile by viewModel.userProfile.collectAsState()

    var weight by remember { mutableStateOf(profile.weightKg.toString()) }
    var height by remember { mutableStateOf(profile.heightCm.toString()) }
    var gender by remember { mutableStateOf(profile.gender) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Your Profile", style = MaterialTheme.typography.headlineSmall)

        OutlinedTextField(
            value = weight,
            onValueChange = { weight = it },
            label = { Text("Weight (kg)") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = height,
            onValueChange = { height = it },
            label = { Text("Height (cm)") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = gender,
            onValueChange = { gender = it },
            label = { Text("Gender (male/female)") },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {
                coroutineScope.launch {
                    viewModel.saveUserProfile(
                        UserProfile(
                            weightKg = weight.toFloatOrNull() ?: 60f,
                            heightCm = height.toFloatOrNull() ?: 165f,
                            gender = gender.lowercase().ifBlank { "female" }
                        )
                    )
                    Toast.makeText(context, "Profile saved!", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save Profile")
        }
    }
}
