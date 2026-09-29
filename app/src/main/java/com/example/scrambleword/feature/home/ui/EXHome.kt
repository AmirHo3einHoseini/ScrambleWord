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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.List
import androidx.compose.material.icons.outlined.School
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.scrambleword.core.ui.theme.WordScrambleTheme
import com.example.scrambleword.core.ui.theme.spacing


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EXHomeScreen(
    onAddWordClick: () -> Unit,
    onPracticeClick: () -> Unit = {},
    onWordListClick: () -> Unit = {},
    onSetting: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "ScrambleWord",
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = MaterialTheme.spacing.default)
                .padding(
                    top = MaterialTheme.spacing.large,
                    bottom = MaterialTheme.spacing.extraLarge
                ),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium)
        ) {
            Text(
                text = "یادگیری کلمه،با ساختن آن",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = MaterialTheme.spacing.small)
            )


            HomeActionCard(
                title = "افزودن کلمه جدید",
                subtitle = "کلمه و معنی را ذخیره کن",
                icon = Icons.Outlined.Add,
                enabled = true,
                onClick = onAddWordClick
            )
// کارت تمرین (فعلاً غیرفعال)
            HomeActionCard(
                title = "شروع تمرین",
                subtitle = "به‌زودی",
                icon = Icons.Outlined.School,
                enabled = false,
                onClick = onPracticeClick
            )

            // کارت لیست کلمات (فعلاً غیرفعال)
            HomeActionCard(
                title = "کلمات من",
                subtitle = "به‌زودی",
                icon = Icons.Outlined.List,
                enabled = false,
                onClick = {}
            )

        }

    }

}


@Composable
private fun HomeActionCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    enabled: Boolean,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        enabled = enabled,
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (enabled) 2.dp else 0.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(MaterialTheme.spacing.cardPadding),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium)
        ) {

            Surface(
                shape = MaterialTheme.shapes.medium,
                color = if (enabled)
                    MaterialTheme.colorScheme.primaryContainer
                else MaterialTheme.colorScheme.surfaceVariant,
                modifier = Modifier.size(48.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = if (enabled)
                            MaterialTheme.colorScheme.onPrimaryContainer
                        else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                    )
                }
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (enabled)
                        MaterialTheme.colorScheme.onSurface
                    else
                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                )
                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(
                        alpha = if (enabled) 1f else 0.5f
                    )
                )
            }


        }


    }
}


@Preview(showBackground = true, locale = "fa")
@Composable
fun HomeScreenPreview() {
    WordScrambleTheme {
        EXHomeScreen(onAddWordClick = {})
    }
}

@Preview(showBackground = true, locale = "fa")
@Composable
fun HomeScreenDarkPreview() {
    WordScrambleTheme(darkTheme = true) {
        EXHomeScreen(onAddWordClick = {})
    }
}

