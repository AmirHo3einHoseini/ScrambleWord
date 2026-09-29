package com.example.scrambleword.feature.addword.ui

import android.R
import android.view.Surface
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.materialIcon
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Lightbulb
import androidx.compose.material.icons.outlined.RecordVoiceOver
import androidx.compose.material.icons.outlined.Translate
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.scrambleword.core.audio.PronunciationPlayer
import com.example.scrambleword.core.ui.theme.GradientAddEnd
import com.example.scrambleword.core.ui.theme.GradientAddStart
import com.example.scrambleword.core.ui.theme.WordScrambleTheme
import com.example.scrambleword.core.ui.theme.spacing
import com.example.scrambleword.domain.model.Language
import com.example.scrambleword.feature.addword.state.AddWordUiState
import com.example.scrambleword.feature.addword.viewModel.AddWordViewModel


private val categories = listOf("عمومی", "سفر", "کار و تحصیل", "غذا", "فناوری")
private val SelectedGlow = Color(0xFF4ADE80)

@Composable
fun AddWordScreen(
    onBack: () -> Unit,
    onOpenDetails: (word: String, languageCode: String) -> Unit,
    onPlayAudio: (url: String) -> Unit = {},
    viewModel: AddWordViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val player = remember { PronunciationPlayer() }
    val snackbarHostState = remember { SnackbarHostState() }

    DisposableEffect(Unit) {
        onDispose { player.stop() }
    }
    LaunchedEffect(uiState.showSavedMessage) {
        if (uiState.showSavedMessage) {
            snackbarHostState.showSnackbar(
                message = "کلمه با موفقیت ذخیره شد.",
                duration = SnackbarDuration.Short
            )
            viewModel.resetSavedState()

        }
    }


    AddWordContent(
        uiState = uiState,
        onWordChange = viewModel::onWordChange,
        onMeaningChange = viewModel::meaningChange,
        onHintChange = viewModel::onHintChange,
        onLanguageSelected = viewModel::onLanguageChange,
        onCategorySelected = viewModel::onCategorySelected,
        onSaveClick = viewModel::saveWord,
        onBack = onBack,
        snackbar = snackbarHostState,
        onOpenDetails = {
            if (uiState.wordInput.isNotBlank()) {
                onOpenDetails(
                    uiState.wordInput.trim(),
                    uiState.selectedLanguage.code
                )
            }
        },
        onPlayAudio = {
            uiState.audioUrl?.let { player.play(it) }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddWordContent(
    uiState: AddWordUiState,
    onWordChange: (String) -> Unit,
    onMeaningChange: (String) -> Unit,
    onHintChange: (String) -> Unit,
    onLanguageSelected: (Language) -> Unit,
    onCategorySelected: (String) -> Unit,
    onSaveClick: () -> Unit,
    onBack: () -> Unit,
    snackbar: SnackbarHostState,
    onOpenDetails: () -> Unit,
    onPlayAudio: () -> Unit,
) {

    var categoryExpanded by remember { mutableStateOf(false) }



    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "افزودن کلمه",
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
            Surface(
                color = MaterialTheme.colorScheme.background,
                tonalElevation = 0.dp
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .windowInsetsPadding(WindowInsets.navigationBars)
                        .windowInsetsPadding(WindowInsets.ime)
                        .padding(horizontal = MaterialTheme.spacing.default)
                        .padding(
                            top = MaterialTheme.spacing.small,
                            bottom = MaterialTheme.spacing.medium
                        )
                ) {
                    Button(
                        onClick = onSaveClick,
                        enabled = !uiState.isSaving,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp),
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

                            if (uiState.isSaving) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(22.dp),
                                    color = Color.White,
                                    strokeWidth = 2.dp
                                )
                            } else {
                                Text(
                                    text = "ذخیره کلمه",
                                    color = Color.White,
                                    style = MaterialTheme.typography.titleMedium
                                )
                            }
                        }
                    }
                }

            }
        },
        snackbarHost = {
            SnackbarHost(
                hostState = snackbar,
                modifier = Modifier.padding(bottom = 72.dp)
            ) { data ->
                Snackbar(
                    snackbarData = data,
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    shape = MaterialTheme.shapes.medium
                )

            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = MaterialTheme.spacing.default)
                .padding(top = MaterialTheme.spacing.medium, bottom = MaterialTheme.spacing.large),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.sectionSpacing)
        ) {
            SectionTitle("زبان کلمه")
            Surface(
                shape = MaterialTheme.shapes.large,
                color = MaterialTheme.colorScheme.surface,
                modifier = Modifier.fillMaxWidth()
            ) {
                LanguageSelector(
                    selected = uiState.selectedLanguage,
                    onSelected = onLanguageSelected,
                    modifier = Modifier.padding(MaterialTheme.spacing.cardPadding)
                )


            }

            SectionTitle("اطلاعات کلمه")
            Column(
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium)
            ) {

                AddWordField(
                    value = uiState.wordInput,
                    onValueChange = onWordChange,
                    label = "کلمه",
                    supporting = "(English)",
                    icon = Icons.Outlined.Edit,
                    singleLine = true,
                    keyboardOption = KeyboardOptions(
                        capitalization = KeyboardCapitalization.None,
                        imeAction = ImeAction.Next
                    )
                )
                Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))

                MoreDetailsCard(
                    word = uiState.wordInput,
                    phonetic = uiState.phonetic,
                    audioUrl = uiState.audioUrl,
                    onOpenDetails = onOpenDetails,
                    onPlayAudio = onPlayAudio
                )

                AddWordField(
                    value = uiState.meaningInput,
                    onValueChange = onMeaningChange,
                    label = "معنی",
                    supporting = "(Persian)",
                    icon = Icons.Outlined.Translate,
                    singleLine = false,
                    minLines = 1,
                    keyboardOption = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Sentences,
                        imeAction = ImeAction.Next
                    )
                )

                ExposedDropdownMenuBox(
                    expanded = categoryExpanded,
                    onExpandedChange = { categoryExpanded = it }
                ) {
                    OutlinedTextField(
                        value = uiState.selectedCategory,
                        onValueChange = {},
                        readOnly = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(),
                        label = { Text("دسته‌بندی") },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = categoryExpanded)
                        },
                        shape = MaterialTheme.shapes.medium,
                        colors = fieldColors()
                    )
                    ExposedDropdownMenu(
                        expanded = categoryExpanded,
                        onDismissRequest = { categoryExpanded = false }
                    ) {
                        categories.forEach { item ->
                            DropdownMenuItem(
                                text = { Text(item) },
                                onClick = {
                                    onCategorySelected(item)
                                    categoryExpanded = false
                                }
                            )
                        }
                    }
                }
                AddWordField(
                    value = uiState.hintInput,
                    onValueChange = onHintChange,
                    label = "راهنمایی",
                    supporting = "اختیاری",
                    icon = Icons.Outlined.Lightbulb,
                    singleLine = true,
                    keyboardOption = KeyboardOptions(imeAction = ImeAction.Done)
                )


            }
            if (uiState.errorMessage != null) {
                Text(
                    text = uiState.errorMessage,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

        }


    }
}


@Composable
private fun SectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleSmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier.padding(start = MaterialTheme.spacing.extraSmall)
    )
}


@Composable
private fun LanguageSelector(
    selected: Language,
    onSelected: (Language) -> Unit,
    modifier: Modifier = Modifier
) {

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Language.entries.forEach { language ->
            val isSelected = selected == language

            val borderWith by animateDpAsState(
                targetValue = if (isSelected) 2.dp else 1.dp,
                animationSpec = tween(220),
                label = "borderWidth"
            )
            val borderColor by animateColorAsState(
                targetValue = if (isSelected) SelectedGlow else MaterialTheme.colorScheme.outline,
                animationSpec = tween(220),
                label = "borderColor"
            )

            val containerColor by animateColorAsState(
                targetValue = if (isSelected)
                    MaterialTheme.colorScheme.surfaceVariant
                else MaterialTheme.colorScheme.surface,
                animationSpec = tween(220),
                label = "container"
            )
            Surface(
                onClick = { onSelected(language) },
                shape = RoundedCornerShape(12.dp),
                color = containerColor,
                modifier = Modifier
                    .weight(1f)
                    .border(
                        width = borderWith,
                        color = borderColor,
                        shape = RoundedCornerShape(12.dp)
                    )
            ) {
                Row(
                    modifier = Modifier
                        .padding(vertical = 12.dp, horizontal = 8.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = language.flag)
                    Spacer(Modifier.width(6.dp))
                    Text(
                        text = language.code.uppercase(),
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

            }
        }
    }
}


@Composable
private fun AddWordField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    supporting: String,
    icon: ImageVector,
    singleLine: Boolean,
    minLines: Int = 1,
    keyboardOption: KeyboardOptions = KeyboardOptions.Default
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        label = { Text(supporting) },
        leadingIcon = {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(18.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        },
        singleLine = singleLine,
        minLines = minLines,
        shape = MaterialTheme.shapes.medium,
        keyboardOptions = keyboardOption,
        colors = fieldColors()
    )


}

@Composable
private fun MoreDetailsCard(
    word: String,
    phonetic: String?,
    audioUrl: String?,
    onOpenDetails: () -> Unit,
    onPlayAudio: () -> Unit
) {
    val hasWord = word.isNotBlank()
    val hasDetails = !phonetic.isNullOrBlank() || !audioUrl.isNullOrBlank()

    Card(
        onClick = { if (hasWord) onOpenDetails() },
        enabled = hasWord,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f)
        ),
        border = BorderStroke(
            width = 1.dp,
            color = if (hasWord)
                MaterialTheme.colorScheme.primary.copy(alpha = 0.45f)
            else
                MaterialTheme.colorScheme.outline.copy(alpha = 0.35f)
        )
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Surface(
                shape = CircleShape,
                color = MaterialTheme.colorScheme.primaryContainer,
                modifier = Modifier.size(40.dp)
            ) {
                Box(
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.RecordVoiceOver,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "جزئیات بیشتر",
                    style = MaterialTheme.typography.titleSmall,
                    color = if (hasWord)
                        MaterialTheme.colorScheme.onSurface
                    else
                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = when {
                        !hasWord -> "اول کلمه را وارد کن"
                        hasDetails -> phonetic ?: "تلفظ آماده است "
                        else -> "تلفظ و املای صحیح"
                    },
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            if (!audioUrl.isNullOrBlank()) {
                IconButton(onClick = onPlayAudio) {
                    Icon(
                        imageVector = Icons.Default.VolumeUp,
                        contentDescription = "پخش تلفظ",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }

        }


    }


}


@Composable
private fun fieldColors() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = MaterialTheme.colorScheme.primary,
    unfocusedBorderColor = MaterialTheme.colorScheme.outline,
    focusedContainerColor = MaterialTheme.colorScheme.surface,
    unfocusedContainerColor = MaterialTheme.colorScheme.surface
)

@Preview(showBackground = true, locale = "fa", name = "AddWord RTL Dark")
@Composable
private fun AddWordRtlPreview() {
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        WordScrambleTheme(darkTheme = false) {
            AddWordContent(
                uiState = AddWordUiState(
                    wordInput = "beautiful",
                    meaningInput = "زیبا",
                    selectedLanguage = Language.ENGLISH,
                    selectedCategory = "عمومی"
                ),
                onWordChange = {},
                onMeaningChange = {},
                onHintChange = {},
                onLanguageSelected = {},
                onCategorySelected = {},
                onSaveClick = {},
                onBack = {},
                snackbar = SnackbarHostState(),
                onOpenDetails = {},
                onPlayAudio = {}
            )
        }
    }
}

