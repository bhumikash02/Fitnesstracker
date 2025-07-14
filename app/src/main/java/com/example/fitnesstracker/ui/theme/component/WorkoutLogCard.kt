package com.example.fitnesstracker.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsRun
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.fitnesstracker.model.WorkoutEntity

@Composable
fun WorkoutLogCard(
    workout: WorkoutEntity
) {
    Row(
        modifier = Modifier
            .padding(vertical = 6.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Icon based on workout type
        val icon = when {
            workout.exerciseName.contains("yoga", ignoreCase = true) -> Icons.Default.SelfImprovement
            else -> Icons.Default.DirectionsRun
        }
        Icon(
            imageVector = icon,
            contentDescription = "Exercise Icon",
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(24.dp)
        )

        Spacer(modifier = Modifier.width(16.dp))

        // Workout details
        Column {
            Text(
                text = "${workout.exerciseName} - ${workout.sets} Sets × ${workout.reps} Reps",
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = MaterialTheme.typography.bodyLarge.fontWeight)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Time: ${workout.duration} mins",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )
        }
    }
}