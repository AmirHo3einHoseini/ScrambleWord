package com.example.scrambleword.feature.home.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.MenuBook
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.TopEnd
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.scrambleword.R
import com.example.scrambleword.core.ui.theme.GradientAddEnd
import com.example.scrambleword.core.ui.theme.GradientAddStart
import com.example.scrambleword.core.ui.theme.GradientPracticeEnd
import com.example.scrambleword.core.ui.theme.GradientPracticeStart
import com.example.scrambleword.core.ui.theme.GradientWordsEnd
import com.example.scrambleword.core.ui.theme.GradientWordsStart
import com.example.scrambleword.core.ui.theme.WordScrambleTheme
import com.example.scrambleword.core.ui.theme.spacing

@Composable
fun HomeScreen(
    onAddWordClick: () -> Unit,
    onPracticeClick: () -> Unit  ,
    onWordListClick: () -> Unit,
    onSettingsClick: () -> Unit = {}
) {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()

        ) {

            IconButton(
                onClick = onSettingsClick,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(MaterialTheme.spacing.small)
                    .statusBarsPadding()
            ) {
                Icon(
                    imageVector = Icons.Outlined.Settings,
                    contentDescription = "تنظیمات",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = MaterialTheme.spacing.large)
                    .padding(top = 64.dp, bottom = MaterialTheme.spacing.extraLarge),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(MaterialTheme.spacing.large))

                Text(
                    text = buildAnnotatedString {
                        withStyle(
                            SpanStyle(
                                color = MaterialTheme.colorScheme.onBackground,
                                fontWeight = FontWeight.Bold
                            )
                        ) {
                            append("Word")
                        }
                        withStyle(
                            SpanStyle(
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Bold
                            )
                        ) {
                            append("Scramble")
                        }
                    },
                    style = MaterialTheme.typography.headlineLarge.copy(fontSize = 32.sp)
                )

                Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))

                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = "یاد بگیر، تمرین کن، پیشرفت کن",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(MaterialTheme.spacing.large))

                Image(
                    painter = painterResource(id = R.drawable.word_scramble_logo),
                    contentDescription = "",
                    modifier = Modifier
                        .size(200.dp)
                        .padding(vertical = MaterialTheme.spacing.medium),
                    contentScale = ContentScale.Fit
                )
                Spacer(modifier = Modifier.height(MaterialTheme.spacing.extraLarge))

                GradientHomeButton(
                    text = " افزودن کلمه جدید",
                    icon = Icons.Outlined.Add,
                    gradient = listOf(GradientAddStart, GradientAddEnd),
                    onClick = onAddWordClick
                )

                Spacer(modifier = Modifier.height(MaterialTheme.spacing.large))

                GradientHomeButton(
                    text = "شروع تمرین",
                    icon = Icons.Outlined.PlayArrow,
                    gradient = listOf(GradientPracticeStart, GradientPracticeEnd),
                    onClick = onPracticeClick
                )

                Spacer(modifier = Modifier.height(MaterialTheme.spacing.large))

                GradientHomeButton(
                    text = "کلمات من",
                    icon = Icons.Outlined.MenuBook,
                    gradient = listOf(GradientWordsStart, GradientWordsEnd),
                    onClick = onWordListClick
                )

            }

        }

    }


}

@Composable
private fun GradientHomeButton(
    text: String,
    icon: ImageVector,
    gradient: List<Color>,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        shape = RoundedCornerShape(28.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor =
                Color.Transparent
        ),
        contentPadding = PaddingValues()
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.horizontalGradient(gradient),
                    shape = RoundedCornerShape(28.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = text,
                    color = Color.White,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
                )

                Spacer(modifier = Modifier.width(10.dp))

                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(22.dp)
                )

            }
        }
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
            onAddWordClick = {},
            onWordListClick = {},
            onPracticeClick = {}
        )
    }
}

@Preview(
    locale = "fa",
    name = "Home RTL"
)
@Composable
fun HomePreview() {
    WordScrambleTheme(darkTheme = false) {

        HomeScreen(
             onAddWordClick = {},
            onWordListClick = {},
            onPracticeClick = {}
        )
    }
}

