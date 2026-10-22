package com.example.scrambleword.feature.home.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.scrambleword.core.ui.theme.WordScrambleTheme

@Composable
fun HomeScreen(
    onOpenSetting: () -> Unit,
    onStartLesson: () -> Unit,
    onOpenPractice: () -> Unit
) {
    Column(
        Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {


        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "سلام 👋",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            IconButton(onClick = onOpenSetting) {
                Icon(
                    Icons.Default.Settings,
                    contentDescription = "تنظیمات",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

        }
        Spacer(Modifier.height(24.dp))

        Box(
            Modifier
                .fillMaxWidth()
                .height(180.dp),
            contentAlignment = Alignment.Center
        ) { }
        Spacer(Modifier.height(16.dp))
        Text(
            text = "هر روز یک قدم کوچک.",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(Modifier.height(16.dp))

        Button(onClick = onStartLesson, modifier = Modifier.fillMaxWidth()) {
            Text("شروع درس ")
        }
        Spacer(Modifier.height(8.dp))
        OutlinedButton(
            onClick = onOpenPractice,
            modifier = Modifier.fillMaxWidth()
        ) { Text("شروع تمرین ") }
    }

}

@Preview(
    showBackground = true,
    locale = "fa",
    name = "Home RTL",
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun HomeDarkPreview() {
    WordScrambleTheme(darkTheme = true) {
        HomeScreen(
            {}, {}, {}
        )
    }
}


@Preview(
    locale = "fa",
    name = "Home RTL"
)
@Composable
private fun lightPreview() {
    WordScrambleTheme(darkTheme = false) {
        HomeScreen(
            onOpenSetting = {},
            onStartLesson = {},
            onOpenPractice = {}
        )
    }
}