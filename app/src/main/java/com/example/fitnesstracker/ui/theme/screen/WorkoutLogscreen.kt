package com.example.fitnesstracker.ui.theme.screen
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.fitnesstracker.model.workoutSuggestions
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

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    Scaffold(snackbarHost = { SnackbarHost(hostState = snackbarHostState) }) { paddingValues ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .padding(paddingValues),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text("Log Workout", style = MaterialTheme.typography.headlineMedium)
                Spacer(modifier = Modifier.height(8.dp))
            }

            item {
                TextField(value = exerciseName, onValueChange = { exerciseName = it }, label = { Text("Exercise Name") }, modifier = Modifier.fillMaxWidth())
                TextField(value = sets, onValueChange = { sets = it }, label = { Text("Sets") }, modifier = Modifier.fillMaxWidth())
                TextField(value = reps, onValueChange = { reps = it }, label = { Text("Reps") }, modifier = Modifier.fillMaxWidth())
                TextField(value = duration, onValueChange = { duration = it }, label = { Text("Duration (mins)") }, modifier = Modifier.fillMaxWidth())
                Button(
                    onClick = {
                        viewModel.addWorkout(
                            name = exerciseName,
                            sets = sets.toIntOrNull() ?: 0,
                            reps = reps.toIntOrNull() ?: 0,
                            duration = duration.toIntOrNull() ?: 0
                        )
                        exerciseName = ""
                        sets = ""
                        reps = ""
                        duration = ""
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar("Workout saved successfully")
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Save Workout")
                }
            }

            item {

                Button(onClick = onHistoryClick, modifier = Modifier.fillMaxWidth()) {
                    Text("View Workout History")
                }
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
                Text("🏋️ Workout Suggestions", style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(8.dp))
            }

            items(workoutSuggestions) { suggestion ->
                WorkoutSuggestionCard(suggestion.name, suggestion.category, suggestion.videoUrl)
            }
        }
    }
}

@Composable
fun WorkoutSuggestionCard(name: String, category: String, videoUrl: String) {
    val context = LocalContext.current
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(videoUrl))
                context.startActivity(intent)
            },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("🏋️ $name", style = MaterialTheme.typography.titleMedium)
            Text("Category: $category", style = MaterialTheme.typography.bodySmall)
            Text("▶ Tap to watch tutorial", style = MaterialTheme.typography.labelSmall)
        }
    }
}
