package com.example.fitnesstracker.ui.theme.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fitnesstracker.viewmodel.StopwatchViewModel
import com.example.fitnesstracker.viewmodel.formatTime

@Composable
fun StopwatchScreen(viewModel: StopwatchViewModel) {
    val timeMillis by viewModel.timeMillis.collectAsState()
    val isRunning by viewModel.isRunning.collectAsState()
    val laps by viewModel.laps.collectAsState()

    // ⭐️ FIX: Read the theme colors here, inside the @Composable context.
    val trackColor = MaterialTheme.colorScheme.surfaceVariant
    val progressColor = MaterialTheme.colorScheme.primary

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "Stopwatch",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.size(250.dp)
        ) {
            val sweepAngle by animateFloatAsState(
                targetValue = (timeMillis % 60000) / 60000f * 360f,
                animationSpec = tween(durationMillis = 100),
                label = "StopwatchArc"
            )

            // The Canvas block is NOT a @Composable context.
            Canvas(modifier = Modifier.fillMaxSize()) {
                // Background track
                drawArc(
                    color = trackColor, // Use the variable we read earlier
                    startAngle = -90f,
                    sweepAngle = 360f,
                    useCenter = false,
                    style = Stroke(width = 20f, cap = StrokeCap.Round)
                )
                // Foreground progress arc
                drawArc(
                    color = progressColor, // Use the variable we read earlier
                    startAngle = -90f,
                    sweepAngle = sweepAngle,
                    useCenter = false,
                    style = Stroke(width = 20f, cap = StrokeCap.Round)
                )
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = formatTime(timeMillis).substringBeforeLast(':'),
                    fontSize = 52.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "." + formatTime(timeMillis).substringAfterLast(':'),
                    fontSize = 24.sp,
                    color = Color.Gray
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            AnimatedVisibility(visible = isRunning) {
                OutlinedButton(onClick = { viewModel.lap() }) {
                    Text("Lap")
                }
            }
            AnimatedVisibility(visible = !isRunning && timeMillis > 0) {
                OutlinedButton(onClick = { viewModel.reset() }) {
                    Text("Reset")
                }
            }
            Button(
                onClick = { if (isRunning) viewModel.pause() else viewModel.start() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isRunning) Color.Red.copy(alpha = 0.7f) else progressColor
                )
            ) {
                Text(text = if (isRunning) "Pause" else "Start")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            modifier = Modifier.fillMaxWidth()
        ) {
            itemsIndexed(laps.reversed()) { index, lapTime ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Lap ${laps.size - index}", color = Color.Gray)
                    Text(formatTime(lapTime))
                }
            }
        }
    }
}