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
    Log.d("ChartDebug", "WeeklyStepChart received data: $weeklySteps")

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
            val maxSteps = weeklySteps.maxOfOrNull { it.steps }?.coerceAtLeast(goal) ?: goal

            if (maxSteps > 0 && weeklySteps.isNotEmpty()) {
                weeklySteps.forEach { stepData ->
                    val progress = stepData.steps.toFloat() / maxSteps.toFloat()

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Bottom,
                        modifier = Modifier.fillMaxHeight()
                    ) {
                        Box(
                            modifier = Modifier
                                .width(28.dp)
                                .fillMaxHeight(progress)
                                .clip(RoundedCornerShape(8.dp))
                                .background(
                                    if (stepData.steps >= goal) Color(0xFFC05EDA) else Color(0xFF730C88)
                                )
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