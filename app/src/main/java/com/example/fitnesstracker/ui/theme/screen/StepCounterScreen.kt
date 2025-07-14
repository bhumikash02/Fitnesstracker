package com.example.fitnesstracker.ui.theme.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.fitnesstracker.ui.components.CircularStepCounter
import com.example.fitnesstracker.ui.components.WeeklyStepChart
import com.example.fitnesstracker.utils.RealTimeStepTracker
import com.example.fitnesstracker.viewmodel.StepViewModel

@Composable
fun StepCounterScreen(viewModel: StepViewModel = viewModel()) {
    // ⭐️ FIX: All the old Health Connect logic (uiState, launchers, etc.) is removed.

    // We just need to collect the state directly from the ViewModel.
    val todaySteps by viewModel.todaySteps.collectAsState()
    val weeklySteps by viewModel.weeklySteps.collectAsState()
    val goal = viewModel.stepGoal

    // Listen for live sensor updates to keep this screen's counter live too.
    val totalSensorSteps by RealTimeStepTracker.totalSteps.observeAsState(0)
    LaunchedEffect(totalSensorSteps) {
        if (totalSensorSteps > 0) {
            viewModel.onSensorStepsChanged(totalSensorSteps)
        }
    }

    // A simple layout to display the data.
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Step Tracker", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(32.dp))

        // Reuse our existing components.
        CircularStepCounter(steps = todaySteps, goal = goal)

        Spacer(modifier = Modifier.height(32.dp))

        WeeklyStepChart(weeklySteps = weeklySteps, goal = goal)
    }
}

// ⭐️ FIX: The old helper composables (HealthConnectNotAvailableScreen, RequestPermissionsScreen)
// are no longer needed and have been deleted.