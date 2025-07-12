package com.example.fitnesstracker.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsRun
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fitnesstracker.model.WorkoutEntity

@Composable
fun WorkoutLogCard(
    workout: WorkoutEntity
) {
    Row(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFFF2F6FA))
            .padding(12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(Color(0xFFE0E7EF))
        ) {
            val icon = when {
                workout.exerciseName.contains("yoga", ignoreCase = true) -> Icons.Default.SelfImprovement
                else -> Icons.Default.DirectionsRun
            }

            androidx.compose.material3.Icon(
                imageVector = icon,
                contentDescription = "Exercise Icon",
                tint = Color.Black,
                modifier = Modifier.padding(10.dp)
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column {
            Text(text = workout.exerciseName, style = MaterialTheme.typography.bodyLarge)
            Text(
                text = "${workout.duration} min",
                color = Color(0xFF5D8BF4),
                fontSize = 14.sp
            )
        }
    }
}
