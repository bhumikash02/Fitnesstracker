package com.example.fitnesstracker.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CircularStepCounter(
    steps: Int,
    goal: Int,
    modifier: Modifier = Modifier
) {
    val progress = if (goal > 0) steps.toFloat() / goal.toFloat() else 0f
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(durationMillis = 1000),
        label = "progressAnimation"
    )

    val trackColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
    val progressBrush = Brush.linearGradient(
        colors = listOf(Color(0xFFC05EDA), Color(0xFF730C88))
    )
    val textColor = MaterialTheme.colorScheme.onSurface

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.size(220.dp)
    ) {
        Canvas(modifier = Modifier.size(220.dp)) {
            // Background track
            drawArc(
                color = trackColor,
                startAngle = -90f,
                sweepAngle = 360f,
                useCenter = false,
                style = Stroke(width = 35f, cap = StrokeCap.Round)
            )
            // Foreground progress
            drawArc(
                brush = progressBrush,
                startAngle = -90f,
                sweepAngle = animatedProgress * 360f,
                useCenter = false,
                style = Stroke(width = 35f, cap = StrokeCap.Round)
            )
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "$steps",
                fontSize = 52.sp,
                fontWeight = FontWeight.Bold,
                color = textColor
            )
            Text(
                text = "Steps Today",
                fontSize = 16.sp,
                color = Color.Gray
            )
        }
    }
}