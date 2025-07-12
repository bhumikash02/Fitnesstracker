package com.example.fitnesstracker.ui.theme.screen
import kotlin.math.roundToInt
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
fun ProfileScreen(
    userProfile: UserProfile,
    onSaveProfile: (UserProfile) -> Unit,
    onLogout: () -> Unit,
    isDarkTheme: Boolean,
    onThemeToggle: () -> Unit
) {
    var name by remember { mutableStateOf(userProfile.name) }
    var age by remember { mutableStateOf(userProfile.age.toString()) }
    var height by remember { mutableStateOf(userProfile.heightCm.toString()) }
    var weight by remember { mutableStateOf(userProfile.weightKg.toString()) }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {

        Text(text = "Profile", style = MaterialTheme.typography.headlineMedium)

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = age,
            onValueChange = { age = it },
            label = { Text("Age") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = height,
            onValueChange = { height = it },
            label = { Text("Height (cm)") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = weight,
            onValueChange = { weight = it },
            label = { Text("Weight (kg)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(text = "BMI: ${userProfile.bmi.roundToInt()}", style = MaterialTheme.typography.bodyLarge)

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Text("Dark Theme")
            Switch(
                checked = isDarkTheme,
                onCheckedChange = { onThemeToggle() }
            )
        }

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

        OutlinedButton(
            onClick = onLogout,
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
        ) {
            Text("Logout")
        }
    }
}
