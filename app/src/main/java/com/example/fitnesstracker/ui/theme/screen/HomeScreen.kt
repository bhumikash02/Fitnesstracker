package com.example.fitnesstracker.ui.theme.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.fitnesstracker.ui.components.CircularStepCounter
import com.example.fitnesstracker.ui.components.WeeklyStepChart
import com.example.fitnesstracker.ui.components.WorkoutLogCard
import com.example.fitnesstracker.ui.theme.Navigation.Routes
import com.example.fitnesstracker.utils.RealTimeStepTracker
import com.example.fitnesstracker.viewmodel.StepViewModel
import com.example.fitnesstracker.viewmodel.WorkoutViewModel

@Composable
fun HomeScreen(
    navController: NavController,
    stepViewModel: StepViewModel,
    workoutViewModel: WorkoutViewModel
) {
    // Collect state from ViewModels
    val todaySteps by stepViewModel.todaySteps.collectAsState()
    val weeklySteps by stepViewModel.weeklySteps.collectAsState()
    val userProfile by stepViewModel.userProfile.collectAsState()
    val stepGoal = stepViewModel.stepGoal
    val recentWorkouts by workoutViewModel.allWorkouts.collectAsState()

    // ⭐️ FIX: The Health Connect logic is removed from here.
    // We only need to initialize the user preferences.
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        stepViewModel.initUserPrefs(context)
    }

    // This part is still correct: it observes the live sensor data.
    val totalSensorSteps by RealTimeStepTracker.totalSteps.observeAsState(0)
    LaunchedEffect(totalSensorSteps) {
        if (totalSensorSteps > 0) {
            stepViewModel.onSensorStepsChanged(totalSensorSteps)
        }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate(Routes.WORKOUT) },
                containerColor = Color(0xFFC05EDA),
                contentColor = Color.White
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Add Workout")
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header Section
            item {
                Spacer(modifier = Modifier.height(32.dp))
                Text(
                    text = "Hi ${userProfile.name.substringBefore(" ")}! 👋",
                    style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.fillMaxWidth()
                )
                Text(
                    text = "Let's Move!",
                    style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Every step counts ✨",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.Gray,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(32.dp))
            }

            // Circular Step Counter
            item {
                CircularStepCounter(steps = todaySteps, goal = stepGoal)
                Spacer(modifier = Modifier.height(32.dp))
            }

            // Weekly Chart
            item {
                WeeklyStepChart(weeklySteps = weeklySteps, goal = stepGoal)
                Spacer(modifier = Modifier.height(24.dp))
            }

            // Recent Workouts Section
            item {
                Text(
                    "Your Workouts 💪",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Display workouts
            if (recentWorkouts.isEmpty()) {
                item {
                    Text(
                        "No workouts logged yet. Tap the '+' to add one!",
                        color = Color.Gray,
                        modifier = Modifier.padding(vertical = 24.dp)
                    )
                }
            } else {
                items(recentWorkouts.take(3)) { workout ->
                    WorkoutLogCard(workout = workout)
                }
            }

            // Padding for FAB
            item {
                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }
}