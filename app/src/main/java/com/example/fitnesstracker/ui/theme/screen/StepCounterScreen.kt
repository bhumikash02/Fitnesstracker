package com.example.fitnesstracker.ui.theme.screen

import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.fitnesstracker.ui.components.CircularStepCounter
import com.example.fitnesstracker.ui.components.WeeklyStepChart
import com.example.fitnesstracker.utils.RealTimeStepTracker
import com.example.fitnesstracker.viewmodel.StepViewModel

// Sealed class to represent the different states of our UI for this screen.
private sealed class StepCounterUiState {
    object Loading : StepCounterUiState()
    object NotAvailable : StepCounterUiState()
    object PermissionRequired : StepCounterUiState()
    object Granted : StepCounterUiState()
}

@Composable
fun StepCounterScreen(viewModel: StepViewModel = viewModel()) {
    val context = LocalContext.current

    // Collect data states from ViewModel
    val todaySteps by viewModel.todaySteps.collectAsState()
    val weeklySteps by viewModel.weeklySteps.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val goal = viewModel.stepGoal

    // --- START OF CORRECTED LOGIC ---
    var uiState by remember { mutableStateOf<StepCounterUiState>(StepCounterUiState.Loading) }
    val healthConnectManager = remember { viewModel.getOrCreateHealthConnectManager(context) }

    // Launcher for Health Connect's permission dialog
    val requestPermissionLauncher = rememberLauncherForActivityResult(
        contract = healthConnectManager.requestPermissionsActivityContract(),
        onResult = { grantedPermissions ->
            if (grantedPermissions.isNotEmpty()) {
                uiState = StepCounterUiState.Granted
                viewModel.loadStepsFromHealthConnect()
            } else {
                Toast.makeText(context, "Permissions are required to see step history.", Toast.LENGTH_LONG).show()
                uiState = StepCounterUiState.PermissionRequired
            }
        }
    )

    // This effect runs once to check permissions and set the initial UI state
    LaunchedEffect(key1 = true) {
        viewModel.initUserPrefs(context)
        if (healthConnectManager.isApiAvailable()) {
            val hasPerms = healthConnectManager.hasAllPermissions()
            if (hasPerms) {
                uiState = StepCounterUiState.Granted
                viewModel.loadStepsFromHealthConnect()
            } else {
                uiState = StepCounterUiState.PermissionRequired
            }
        } else {
            uiState = StepCounterUiState.NotAvailable
        }
    }
    // --- END OF CORRECTED LOGIC ---


    // This effect listens for live sensor data
    val totalSensorSteps by RealTimeStepTracker.totalSteps.observeAsState(0)
    LaunchedEffect(totalSensorSteps) {
        if (totalSensorSteps > 0) {
            viewModel.onSensorStepsChanged(totalSensorSteps)
        }
    }

    // Main UI rendering based on the current state
    when (uiState) {
        is StepCounterUiState.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        is StepCounterUiState.NotAvailable -> {
            HealthConnectNotAvailableScreen()
        }
        is StepCounterUiState.PermissionRequired -> {
            RequestPermissionsScreen {
                requestPermissionLauncher.launch(healthConnectManager.permissions)
            }
        }
        is StepCounterUiState.Granted -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Step Tracker", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(32.dp))

                CircularStepCounter(steps = todaySteps, goal = goal)

                Spacer(modifier = Modifier.height(32.dp))

                if (isLoading) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                } else {
                    WeeklyStepChart(weeklySteps = weeklySteps, goal = goal)
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(onClick = { viewModel.loadStepsFromHealthConnect() }) {
                    Text("Refresh Data")
                }
            }
        }
    }
}

// NOTE: These helper composables are now defined in StepCounterScreen.kt
// and can be made public if another screen needs them, but for now, they are fine here.
@Composable
fun HealthConnectNotAvailableScreen() {
    val context = LocalContext.current
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Health Connect Not Available", style = MaterialTheme.typography.titleLarge, textAlign = TextAlign.Center)
        Text(
            "To use this feature, please install the Health Connect app from the Play Store.",
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(16.dp)
        )
        Button(onClick = {
            val playStoreIntent = Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.google.android.apps.healthdata"))
            context.startActivity(playStoreIntent)
        }) {
            Text("Go to Play Store")
        }
    }
}

@Composable
fun RequestPermissionsScreen(onGrantClick: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Permissions Required", style = MaterialTheme.typography.titleLarge)
        Text("This app needs permission to access your step data from Health Connect.", textAlign = TextAlign.Center, modifier = Modifier.padding(16.dp))
        Button(onClick = onGrantClick) {
            Text("Grant Permissions")
        }
    }
}