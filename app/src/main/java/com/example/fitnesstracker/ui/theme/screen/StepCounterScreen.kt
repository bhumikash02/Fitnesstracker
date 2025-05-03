package com.example.fitnesstracker.ui.theme.screen

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.livedata.observeAsState
import com.example.fitnesstracker.utils.GoogleFitManager
import com.example.fitnesstracker.utils.RealTimeStepTracker
import com.example.fitnesstracker.viewmodel.StepViewModel
import com.example.fitnesstracker.ui.theme.chart.StepBarChart

@Composable
fun StepCounterScreen(viewModel: StepViewModel = viewModel()) {
    val context = LocalContext.current
    val activity = context as Activity

    // Step States
    val todaySteps by viewModel.todaySteps.collectAsState()
    val weeklySteps by viewModel.weeklySteps.collectAsState()
    val calories by viewModel.estimatedCalories.collectAsState()
    val goal = viewModel.stepGoal

    // Sensor Tracking
    val sensorSteps = RealTimeStepTracker.steps.observeAsState(0).value

    // Animations
    val animatedSteps by animateIntAsState(targetValue = todaySteps, label = "stepAnim")
    val progress = animatedSteps.coerceAtMost(goal).toFloat() / goal
    val animatedProgress by animateFloatAsState(targetValue = progress, label = "progressAnim")

    // Request Activity Recognition Permission
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {
                Toast.makeText(context, "Permission Granted ✅", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Permission Denied ❌", Toast.LENGTH_SHORT).show()
            }
        }
    )

    // LaunchedEffect for Initialization
    LaunchedEffect(Unit) {
        // Step 1: Check Permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACTIVITY_RECOGNITION)
                != PackageManager.PERMISSION_GRANTED
            ) {
                permissionLauncher.launch(Manifest.permission.ACTIVITY_RECOGNITION)
            }
        }

        // Step 2: Start RealTime Step Tracking
        RealTimeStepTracker.startTracking(context)

        // Step 3: Initialize User Preferences
        viewModel.initUserPrefs(context)

        // Step 4: Request Google Fit Permissions + Fetch Steps
        GoogleFitManager.checkPermissions(context, activity) { account ->
            viewModel.loadSteps(context, account)
        }
    }

    // Toast 🎉 if Goal Achieved
    LaunchedEffect(animatedSteps) {
        if (animatedSteps >= goal) {
            Toast.makeText(context, "🎉 Goal Achieved! Great job!", Toast.LENGTH_LONG).show()
        }
    }

    // UI
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Step Tracker", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))

        // 🟦 Circular Progress Indicator
        Box(contentAlignment = Alignment.Center, modifier = Modifier.size(180.dp)) {
            Canvas(modifier = Modifier.size(160.dp)) {
                drawArc(
                    color = Color.LightGray,
                    startAngle = -90f,
                    sweepAngle = 360f,
                    useCenter = false,
                    style = Stroke(width = 20f, cap = StrokeCap.Round)
                )
                drawArc(
                    color = Color(0xFF4CAF50),
                    startAngle = -90f,
                    sweepAngle = animatedProgress * 360f,
                    useCenter = false,
                    style = Stroke(width = 20f, cap = StrokeCap.Round)
                )
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("$animatedSteps", fontSize = 32.sp, fontWeight = FontWeight.Bold)
                Text("of $goal steps", fontSize = 16.sp, color = Color.Gray)
            }
        }

        Spacer(modifier = Modifier.height(8.dp))
        Text("Calories Burned: ${"%.1f".format(calories)} kcal", fontSize = 16.sp)
        Text("Sensor Steps (Real-Time): $sensorSteps", fontSize = 14.sp, color = Color.Gray)

        Spacer(modifier = Modifier.height(24.dp))

        // 📊 Weekly Trends Chart
        Text("Weekly Trends", fontWeight = FontWeight.SemiBold)
        StepBarChart(weeklySteps = weeklySteps)

        Spacer(modifier = Modifier.height(24.dp))

        Button(onClick = {
            GoogleFitManager.checkPermissions(context, activity) { account ->
                viewModel.loadSteps(context, account)
            }
        }) {
            Text("Refresh Data")
        }
    }
}
