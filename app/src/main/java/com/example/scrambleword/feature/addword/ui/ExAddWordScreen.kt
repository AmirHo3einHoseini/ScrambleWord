package com.example.scrambleword.feature.addword.ui


import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Translate
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.scrambleword.core.ui.theme.WordScrambleTheme
import com.example.scrambleword.core.ui.theme.*
import com.example.scrambleword.domain.model.Language
import com.example.scrambleword.feature.addword.state.AddWordUiState
import com.example.scrambleword.feature.addword.viewModel.AddWordViewModel

@Composable
fun ExAddWordScreen(
    onBack: () -> Unit,
    onWordSaved: () -> Unit,
    vm: AddWordViewModel = hiltViewModel()
) {
    val uiState by vm.uiState.collectAsState()

    LaunchedEffect(uiState.isSaved) {
        if (uiState.isSaved) {
//            vm.resetSavedState()
            onWordSaved()
        }
    }

    // Pass the state to a stateless Composable to allow for Previews
    addWordContent(
        uiState = uiState,
        onWordChange = vm::onWordChange,
        onMeaningChange = vm::meaningChange,
        onHintChange = vm::onHintChange,
        onLanguageSelected = vm::onLanguageChange,
        onSaveClick = vm::saveWord,
        onBack = onBack
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun addWordContent(
    uiState: AddWordUiState,
    onWordChange: (String) -> Unit,
    onMeaningChange: (String) -> Unit,
    onHintChange: (String) -> Unit,
    onLanguageSelected: (Language) -> Unit,
    onSaveClick: () -> Unit,
    onBack: () -> Unit

) {
    Scaffold(
        modifier = Modifier. wrapContentHeight(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "افزودن کلمه جدید",
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                navigationIcon = {
                    IconButton(modifier = Modifier.size(32.dp), onClick = onBack) {
                        Icon(
                            modifier = Modifier
                                .size(28.dp)
                                .padding(MaterialTheme.spacing.extraSmall),
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "بازگشت"
                        )
                    }

                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        },
        bottomBar = {
            Surface(
                modifier = Modifier.height(68.dp),
                tonalElevation = 2.dp,
                shadowElevation = 4.dp,
                color = MaterialTheme.colorScheme.surface
            ) {
                Button(
                    onClick = onSaveClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .imePadding()
                        .navigationBarsPadding()
                        .padding(horizontal = MaterialTheme.spacing.default)
                        .padding(
                            top = MaterialTheme.spacing.small,
                            bottom = MaterialTheme.spacing.medium
                        ),
                    enabled = !uiState.isSaving,
                    shape = MaterialTheme.shapes.medium
                ) {
                    if (uiState.isSaving) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(22.dp),
                            color = MaterialTheme.colorScheme.onPrimary,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "",
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(MaterialTheme.spacing.small))

                        Text(
                            text = "ذخیره کلمه",
                            style = MaterialTheme.typography.labelLarge
                        )
                    }

                }
            }
        },

        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = MaterialTheme.spacing.default)
                .padding(bottom = MaterialTheme.spacing.extraLarge),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.sectionSpacing)
        ) {
            sectionCard(title = "زبان کلمه") {
                LanguageSelector(
                    selected = uiState.selectedLanguage,
                    onSelected = onLanguageSelected
                )
            }

            sectionCard(title = "اطلاعات کلمات") {
                Column(
                    verticalArrangement =
                        Arrangement.spacedBy(MaterialTheme.spacing.medium)
                ) {

                    OutlinedTextField(
                        value = uiState.wordInput,
                        onValueChange = onWordChange,
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("کلمه") },
                        placeholder = { Text("beautiful") },
                        singleLine = true,
                        shape = MaterialTheme.shapes.medium,
                        keyboardOptions = KeyboardOptions(
                            capitalization = KeyboardCapitalization.None,
                            imeAction = ImeAction.Next
                        ),
                        isError = uiState.errorMessage != null && uiState.meaningInput.isBlank()
                    )


                    OutlinedTextField(
                        value = uiState.meaningInput,
                        onValueChange = onMeaningChange,
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("معنی") },
                        placeholder = { Text("معنی به فارسی یا زبان خودت") },
                        minLines = 1,
                        maxLines = 2,
                        shape = MaterialTheme.shapes.medium,
                        keyboardOptions = KeyboardOptions(
                            capitalization = KeyboardCapitalization.Sentences,
                            imeAction = ImeAction.Next
                        ),
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Outlined.Translate,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                        },
                        isError = uiState.errorMessage != null && uiState.meaningInput.isBlank()
                    )

                    OutlinedTextField(
                        value = uiState.hintInput,
                        onValueChange = onHintChange,
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("راهنمایی (اختیاری") },
                        placeholder = { Text("یک راهنمایی کوتاه برای تمرین") },
                        singleLine = true,
                        shape = MaterialTheme.shapes.medium,

                        )
                }
            }

            if (uiState.errorMessage != null) {
                Text(
                    text = uiState.errorMessage,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier.padding(horizontal = MaterialTheme.spacing.small)
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.CheckCircle,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = "املای صحیح",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "",
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.SemiBold
                ),
                color = MaterialTheme.colorScheme.onSurface
            )


        }
    }
}

@Composable
private fun sectionCard(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)) {

        Text(
            text = title,
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier
                .padding(start = MaterialTheme.spacing.extraSmall)
                .padding(top = MaterialTheme.spacing.medium)
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.large,
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
        ) {
            Column(
                modifier = Modifier.padding(MaterialTheme.spacing.cardPadding),
                content = content
            )
        }


    }
}

@Composable
fun LanguageSelector(
    selected: Language,
    onSelected: (Language) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)
    ) {
        Language.entries.forEach { language ->
            val isSelected = selected == language
            val borderWidth by animateDpAsState(
                targetValue = if (isSelected) 2.dp else 0.dp,
                animationSpec = tween(durationMillis = 280),
                label = "borderWidth"
            )
            val borderColor by animateColorAsState(
                targetValue = if (isSelected) Color(0xFF4ADE80) else Color.Transparent, // سبز روشن
                animationSpec = tween(durationMillis = 280),
                label = "borderColor"
            )
            FilterChip(
                selected = isSelected,
                onClick = { onSelected(language) },
                label = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(text = language.flag)
                        Text(
                            text = language.code.uppercase(),
                            style = MaterialTheme.typography.labelLarge
                        )
                    }

                },
                shape = MaterialTheme.shapes.small,
                modifier = Modifier.border(
                    width = borderWidth,
                    color = borderColor,
                    shape = MaterialTheme.shapes.small
                )
            )
        }
    }
}


@Preview(showBackground = true, locale = "fa")
@Composable
private fun AddWordContentPreview() {
    WordScrambleTheme {
        addWordContent(
            uiState = AddWordUiState(
                wordInput = "beautiful",
                meaningInput = "زیبا",
                hintInput = "مربوط به ظاهر",
                selectedLanguage = Language.ENGLISH
            ),
            onWordChange = {},
            onMeaningChange = {},
            onHintChange = {},
            onLanguageSelected = {},
            onSaveClick = {},
            onBack = {}
        )
    }
}
