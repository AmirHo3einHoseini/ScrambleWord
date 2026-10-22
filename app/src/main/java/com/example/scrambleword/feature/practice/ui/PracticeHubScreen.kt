package com.example.scrambleword.feature.practice.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun PracticeHubScreen(
    onOpenSpellingSetup: () -> Unit,
    onOpenFillBlankSetup: () -> Unit
) {
    Column(Modifier
        .fillMaxSize()
        .padding(20.dp)) {
        Text("تمرین", style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(16.dp))
        Button(
            onClick = onOpenSpellingSetup,
            modifier = Modifier.fillMaxWidth()
        ) { Text("املای کلمه") }
        Spacer(Modifier.height(8.dp))
        Button(
            onClick = onOpenFillBlankSetup,
            modifier = Modifier.fillMaxWidth()
        ) { Text("جای‌خالی") }
    }
}