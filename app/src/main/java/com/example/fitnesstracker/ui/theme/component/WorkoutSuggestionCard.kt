package com.example.fitnesstracker.ui.theme.component

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.fitnesstracker.model.WorkoutSuggestion

@Composable
fun WorkoutSuggestionCard(suggestion: WorkoutSuggestion, onWatchClick: (String) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(modifier = Modifier.padding(12.dp)) {
            AsyncImage(
                model = suggestion.thumbnailUrl,
                contentDescription = null,
                modifier = Modifier
                    .size(100.dp)
                    .padding(end = 12.dp)
            )
            Column {
                Text(suggestion.name, style = MaterialTheme.typography.titleMedium)
                Text("Duration: ${suggestion.duration}", style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.height(8.dp))
                Button(onClick = { onWatchClick(suggestion.videoUrl) }) {
                    Text("Watch Tutorial")
                }
            }
        }
    }
}
