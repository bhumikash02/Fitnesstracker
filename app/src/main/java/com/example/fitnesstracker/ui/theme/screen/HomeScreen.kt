package com.example.fitnesstracker.ui.theme.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import com.example.fitnesstracker.ui.theme.FitnesstrackerTheme

@Composable
fun HomeScreen(
    onWorkoutClick: () -> Unit,
    onBmiClick: () -> Unit,
    onStepsClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Welcome to FitTrack!",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        DashboardCard(
            title = "Step Counter",
            description = "Track your daily steps 👟",
            onClick = onStepsClick
        )

        DashboardCard(
            title = "Workout Logs",
            description = "Log your exercises 💪",
            onClick = onWorkoutClick
        )

        DashboardCard(
            title = "BMI Calculator",
            description = "Check your health stats ⚖️",
            onClick = onBmiClick
        )
    }
}

@Composable
fun DashboardCard(title: String, description: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = title, style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = description, fontSize = 14.sp)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    FitnesstrackerTheme {
        HomeScreen({}, {}, {})
    }
}
