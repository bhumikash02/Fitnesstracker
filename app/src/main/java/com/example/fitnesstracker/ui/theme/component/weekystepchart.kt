package com.example.fitnesstracker.ui.components

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.fitnesstracker.model.StepData

@Composable
fun WeeklyStepChart(
    weeklySteps: List<StepData>,
    goal: Int,
    modifier: Modifier = Modifier
) {
    Log.d("ChartDebug", "WeeklyStepChart received data for ${weeklySteps.size} days: $weeklySteps")

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .height(180.dp)
    ) {
        Text(
            text = "Daily",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
        )
        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.Bottom
        ) {
            val maxSteps = goal.coerceAtLeast(1) // Ensure maxSteps is at least 1 to avoid division by zero

            if (weeklySteps.isNotEmpty()) {
                // Use forEachIndexed to know which bar is the last one (today).
                weeklySteps.forEachIndexed { index, stepData ->

                    // --- START OF VISUAL LOGIC FIX ---

                    // 1. Determine if the current bar represents today.
                    val isToday = index == weeklySteps.lastIndex

                    // 2. Set the bar color. Today is bright purple, all other days are muted gray.
                    val barColor = if (isToday) Color(0xFF9336E3) else Color(0xFF4A4A4A)

                    // 3. Calculate bar progress.
                    val progress = (stepData.steps.toFloat() / maxSteps.toFloat())

                    // 4. Ensure a minimum height for all bars so they are always visible.
                    //    Days with 0 steps will have a small "dummy" bar.
                    //    Days with very few steps will also be slightly larger to be noticeable.
                    val barHeight = progress.coerceAtLeast(0.09f)

                    // --- END OF VISUAL LOGIC FIX ---


                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Bottom,
                        modifier = Modifier.fillMaxHeight()
                    ) {
                        Box(
                            modifier = Modifier
                                .width(28.dp)
                                .fillMaxHeight(barHeight) // Use the new height logic
                                .clip(RoundedCornerShape(8.dp))
                                .background(barColor) // Use the new color logic
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = stepData.day,
                            style = MaterialTheme.typography.bodySmall,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}