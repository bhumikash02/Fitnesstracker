package com.example.fitnesstracker.ui.theme.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.fitnesstracker.viewmodel.WorkoutViewModel
import kotlinx.coroutines.launch

@Composable
fun WorkoutLogScreen(
    viewModel: WorkoutViewModel,
    onBmiClick: () -> Unit = {},
    onHistoryClick: () -> Unit = {}
) {
    var exerciseName by remember { mutableStateOf("") }
    var sets by remember { mutableStateOf("") }
    var reps by remember { mutableStateOf("") }
    var duration by remember { mutableStateOf("") }

    // ✅ Snackbar setup
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .padding(paddingValues)
        ) {
            Text("Log Workout", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                value = exerciseName,
                onValueChange = { exerciseName = it },
                label = { Text("Exercise Name") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            TextField(
                value = sets,
                onValueChange = { sets = it },
                label = { Text("Sets") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            TextField(
                value = reps,
                onValueChange = { reps = it },
                label = { Text("Reps") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            TextField(
                value = duration,
                onValueChange = { duration = it },
                label = { Text("Duration (mins)") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    viewModel.addWorkout(
                        name = exerciseName,
                        sets = sets.toIntOrNull() ?: 0,
                        reps = reps.toIntOrNull() ?: 0,
                        duration = duration.toIntOrNull() ?: 0
                    )

                    // Reset input fields
                    exerciseName = ""
                    sets = ""
                    reps = ""
                    duration = ""

                    // ✅ Show Snackbar
                    coroutineScope.launch {
                        snackbarHostState.showSnackbar("Workout saved successfully ✅")
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save Workout")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = onBmiClick, modifier = Modifier.fillMaxWidth()) {
                Text("Go to BMI Calculator")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = onHistoryClick, modifier = Modifier.fillMaxWidth()) {
                Text("View Workout History")
            }
        }
    }
}
