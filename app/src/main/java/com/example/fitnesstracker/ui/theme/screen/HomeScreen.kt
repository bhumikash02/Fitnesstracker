package com.example.fitnesstracker.ui.theme.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fitnesstracker.R

@Composable
fun HomeScreen(
    onStepsClick: () -> Unit,
    onWorkoutClick: () -> Unit,
    onBmiClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Text("Welcome to FitLife ", style = MaterialTheme.typography.headlineMedium)

        DashboardCard(
            icon = R.drawable.ic_steps,
            title = "Step Tracker",
            description = "View your daily and weekly steps",
            onClick = onStepsClick
        )

        DashboardCard(
            icon = R.drawable.ic_workout,
            title = "Workout Logs",
            description = "Log and view your workouts",
            onClick = onWorkoutClick
        )

        DashboardCard(
            icon = R.drawable.ic_bmi,
            title = "BMI Calculator",
            description = "Check your Body Mass Index",
            onClick = onBmiClick
        )
    }
}

@Composable
fun DashboardCard(
    icon: Int,
    title: String,
    description: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Image(
                painter = painterResource(id = icon),
                contentDescription = title,
                modifier = Modifier.size(48.dp)
            )
            Column {
                Text(title, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Text(description, fontSize = 14.sp)
            }
        }
    }
}
