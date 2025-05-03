package com.example.fitnesstracker.ui.theme.screen


import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import com.example.fitnesstracker.ui.theme.FitnesstrackerTheme

@Composable
fun BmiCalculatorScreen() {
    var height by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }
    var bmi by remember { mutableStateOf<Float?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Text("BMI Calculator", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        TextField(value = height, onValueChange = { height = it }, label = { Text("Height (cm)") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(8.dp))

        TextField(value = weight, onValueChange = { weight = it }, label = { Text("Weight (kg)") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            val h = height.toFloatOrNull()
            val w = weight.toFloatOrNull()
            bmi = if (h != null && w != null) {
                val heightInM = h / 100
                w / (heightInM * heightInM)
            } else null
        }, modifier = Modifier.fillMaxWidth()) {
            Text("Calculate BMI")
        }

        Spacer(modifier = Modifier.height(16.dp))

        bmi?.let {
            Text("Your BMI is %.2f".format(it))
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun BmiCalculatorPreview() {
    FitnesstrackerTheme {
        BmiCalculatorScreen()
    }
}
