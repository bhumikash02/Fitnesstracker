package com.example.fitnesstracker.ui.theme.chart

import android.graphics.Color
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.compose.runtime.Composable
import androidx.compose.ui.viewinterop.AndroidView
import com.example.fitnesstracker.model.StepData
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*

@Composable
fun StepBarChart(weeklySteps: List<StepData>) {
    AndroidView(
        factory = { context ->
            BarChart(context).apply {
                layoutParams = FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    400
                )
                setFitBars(true)
                description = Description().apply { text = "Steps by Day" }
                setDrawGridBackground(false)
                legend.isEnabled = false

                axisRight.isEnabled = false
                xAxis.position = XAxis.XAxisPosition.BOTTOM
                xAxis.granularity = 1f
            }
        },
        update = { chart ->
            val entries = weeklySteps.mapIndexed { index, stepData ->
                BarEntry(index.toFloat(), stepData.steps.toFloat())
            }

            val barDataSet = BarDataSet(entries, "Steps").apply {
                color = Color.rgb(33, 150, 243)
                valueTextColor = Color.WHITE
                valueTextSize = 12f
            }

            chart.data = BarData(barDataSet)
            chart.xAxis.valueFormatter =
                com.github.mikephil.charting.formatter.IndexAxisValueFormatter(weeklySteps.map { it.day })
            chart.invalidate()
        }
    )
}
