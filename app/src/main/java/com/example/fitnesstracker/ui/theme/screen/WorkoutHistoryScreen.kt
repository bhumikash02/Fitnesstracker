package com.example.fitnesstracker.ui.theme.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.fitnesstracker.model.WorkoutEntity
import com.example.fitnesstracker.viewmodel.WorkoutViewModel

@Composable
fun WorkoutHistoryScreen(viewModel: WorkoutViewModel) {
    val workouts by viewModel.allWorkouts.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        items(workouts) { workout ->
            EditableWorkoutCard(workout = workout, viewModel = viewModel)
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@Composable
fun EditableWorkoutCard(workout: WorkoutEntity, viewModel: WorkoutViewModel) {
    var isEditing by remember { mutableStateOf(false) }

    var updatedName by remember { mutableStateOf(workout.exerciseName) }
    var updatedSets by remember { mutableStateOf(workout.sets.toString()) }
    var updatedReps by remember { mutableStateOf(workout.reps.toString()) }
    var updatedDuration by remember { mutableStateOf(workout.duration.toString()) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            if (isEditing) {
                TextField(value = updatedName, onValueChange = { updatedName = it }, label = { Text("Exercise") })
                Spacer(modifier = Modifier.height(4.dp))
                TextField(value = updatedSets, onValueChange = { updatedSets = it }, label = { Text("Sets") })
                Spacer(modifier = Modifier.height(4.dp))
                TextField(value = updatedReps, onValueChange = { updatedReps = it }, label = { Text("Reps") })
                Spacer(modifier = Modifier.height(4.dp))
                TextField(value = updatedDuration, onValueChange = { updatedDuration = it }, label = { Text("Duration") })
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = {
                        val updatedWorkout = workout.copy(
                            exerciseName = updatedName,
                            sets = updatedSets.toIntOrNull() ?: 0,
                            reps = updatedReps.toIntOrNull() ?: 0,
                            duration = updatedDuration.toIntOrNull() ?: 0
                        )
                        viewModel.updateWorkout(updatedWorkout)
                        isEditing = false
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Save")
                }
            } else {
                Text("Exercise: ${workout.exerciseName}", style = MaterialTheme.typography.titleMedium)
                Text("Sets: ${workout.sets}, Reps: ${workout.reps}")
                Text("Duration: ${workout.duration} mins")
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    IconButton(onClick = { isEditing = true }) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit")
                    }
                    IconButton(onClick = { viewModel.deleteWorkout(workout) }) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete")
                    }
                }
            }
        }
    }
}
