package com.example.scrambleword.feature.addword.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect

import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.stylusHoverIcon
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.scrambleword.core.audio.PronunciationPlayer
import com.example.scrambleword.core.ui.theme.GradientAddEnd
import com.example.scrambleword.core.ui.theme.GradientAddStart
import com.example.scrambleword.core.ui.theme.WordScrambleTheme
import com.example.scrambleword.domain.model.DictionaryInfo
import com.example.scrambleword.domain.model.Language
import com.example.scrambleword.feature.addword.state.DictionaryUiState
import com.example.scrambleword.feature.addword.viewModel.DictionaryViewModel


@Composable
fun DictionaryScreen(
    onBack: () -> Unit,
    onUseInForm: (info: DictionaryInfo, selectedTranslation: String?) -> Unit,
    onPlayAudio: (String) -> Unit = {},
    viewModel: DictionaryViewModel = hiltViewModel()
) {


    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val player = remember { PronunciationPlayer() }

    DisposableEffect(Unit) {
        onDispose { player.stop() }
    }


    DictionaryContent(
        uiState,
        onBack,
        onUseInForm = {
            val info = uiState.result ?: return@DictionaryContent

            onUseInForm(info, uiState.selectedTranslation)
        },
        onPlayAudio = {
            uiState.result?.audioUrl?.let { player.play(it) }
        },
        viewModel::onTranslationSelected,
        viewModel::onExampleSelected
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DictionaryContent(
    uiState: DictionaryUiState,
    onBack: () -> Unit,
    onUseInForm: () -> Unit,
    onPlayAudio: () -> Unit,
    onTranslationSelected: (String) -> Unit,
    onExampleSelected: (String) -> Unit
) {


    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "جزئیات بیشتر",
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "بازگشت"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground,
                    navigationIconContentColor = MaterialTheme.colorScheme.onBackground
                )
            )
        },
        bottomBar = {
            if (uiState.result != null) {
                Surface(color = MaterialTheme.colorScheme.background) {
                    Button(
                        onClick = onUseInForm,
                        modifier = Modifier
                            .fillMaxWidth()
                            .navigationBarsPadding()
                            .padding(horizontal = 16.dp)
                            .padding(bottom = 16.dp, top = 8.dp)
                            .height(52.dp),
                        shape = RoundedCornerShape(50),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                        contentPadding = PaddingValues()
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    brush = Brush.horizontalGradient(
                                        listOf(GradientAddStart, GradientAddEnd)
                                    ),
                                    shape = RoundedCornerShape(50)
                                ),
                            contentAlignment = Alignment.Center
                        ) {

                            Text(
                                text = "استفاده در فرم افزودن",
                                color = Color.White,
                                style = MaterialTheme.typography.titleMedium
                            )

                        }
                    }
                }
            }
        }


    ) { innerPading ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPading)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
                .padding(top = 8.dp, bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            when {
                uiState.isLoading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 48.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            color = MaterialTheme.colorScheme.primary,
                            strokeWidth = 3.dp,
                            modifier = Modifier.size(36.dp)
                        )
                    }
                }

                uiState.errorMessage != null -> {
                    Text(
                        text = uiState.errorMessage,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(top = 24.dp)
                    )

                }

                uiState.result != null -> {
                    val info = uiState.result
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp)
                        ) {
                            Text(
                                text = info.word,
                                style = MaterialTheme.typography.headlineMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 28.sp
                                ),
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Start
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                if (!info.audioUrl.isNullOrBlank()) {
                                    FilledIconButton(
                                        onClick = onPlayAudio,
                                        modifier = Modifier.size(44.dp),
                                        shape = CircleShape,
                                        colors = IconButtonDefaults.filledIconButtonColors(
                                            containerColor = MaterialTheme.colorScheme.primary,
                                            contentColor = MaterialTheme.colorScheme.onPrimary
                                        )
                                    ) {
                                        Icon(
                                            imageVector = Icons.Filled.VolumeUp,
                                            contentDescription = "پخش تلفظ",
                                            modifier = Modifier.size(22.dp)
                                        )
                                    }
                                }

                                if (!info.phonetic.isNullOrBlank()) {
                                    Text(
                                        text = info.phonetic,
                                        style = MaterialTheme.typography.titleMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }

                                Spacer(modifier = Modifier.height(16.dp))

                                AssistChip(
                                    onClick = {},
                                    enabled = false,
                                    label = {
                                        Text(
                                            text = "${info.word.let { uiState.selectedLanguage.flag }}${uiState.selectedLanguage.code.uppercase()}"
                                        )
                                    }
                                )

                            }
                        }

                    }


                    val translationOptions = when {
                        info.translationsFa.isNotEmpty() -> info.translationsFa
                        info.definitions.isNotEmpty() -> info.definitions
                        else -> emptyList()
                    }
                    val translationTitle = if (info.translationsFa.isNotEmpty()) {
                        "ترجمه فارسی(یکی را انتخاب کن)"
                    } else {
                        "معنی انگلیسی(یکی را انتخاب کن)"
                    }


                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp)
                        ) {

                            if (translationOptions.isNotEmpty()) {
                                SelectionSection(
                                    title = translationTitle,
                                    options = translationOptions,
                                    selected = uiState.selectedTranslation,
                                    onSelect = onTranslationSelected,
                                )
                            }
                            if (info.examples.isNotEmpty()) {
                                SelectionSection(
                                    title = "مثال (اختیاری)",
                                    options = info.examples,
                                    selected = uiState.selectedExample,
                                    onSelect = onExampleSelected
                                )
                            }
                        }
                    }
                }
            }


        }
    }


}


@Composable
private fun SelectionSection(
    title: String,
    options: List<String>,
    selected: String?,
    onSelect: (String) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {

        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {


            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            options.forEach { item ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onSelect(item) }
                        .padding(vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = selected == item,
                        onClick = { onSelect(item) }
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = item,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.weight(1f)
                    )
                }

            }
        }
    }

}

@Preview(showBackground = true, locale = "fa", name = "Dictionary RTL")
@Composable
private fun DictionaryPreview() {
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        WordScrambleTheme(darkTheme = true) {
            DictionaryContent(
                uiState = DictionaryUiState(
                    selectedLanguage = Language.ENGLISH,
                    result = DictionaryInfo(
                        word = "beautiful",
                        phonetic = "/ˈbjuː.tɪ.fəl/",
                        audioUrl = "https://example.com/a.mp3"
                    )
                ),
                onBack = {},
                onUseInForm = {},
                onPlayAudio = {},
                onTranslationSelected = {},
                onExampleSelected = {}
            )
        }
    }
}