package com.example.scrambleword.feature.lesson

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun LearnHubScreen(
    onStartRestaurantLesson: () -> Unit
) {
    Column(Modifier
        .fillMaxSize()
        .padding(20.dp)) {
        Text("یادگیری", style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(16.dp))
        Card(onClick = onStartRestaurantLesson, modifier = Modifier.fillMaxWidth()) {
            Column(Modifier.padding(16.dp)) {
                Text("رستوران · A1", fontWeight = FontWeight.SemiBold)
                Text("۵ قدم · شروع درس")
            }
        }
    }
}