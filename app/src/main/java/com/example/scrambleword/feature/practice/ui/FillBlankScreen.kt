package com.example.scrambleword.feature.practice.ui


import android.R
import android.R.attr.fontWeight
import android.R.string.ok
import android.widget.Space
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.scrambleword.core.ui.theme.WordScrambleTheme
import com.example.scrambleword.core.ui.theme.spacing
import com.example.scrambleword.data.remote.exercise.FillBlankExerciseDto
import com.example.scrambleword.data.remote.exercise.FillBlankMode
import com.example.scrambleword.domain.model.practice.PracticeDifficulty
import com.example.scrambleword.feature.practice.state.FillBlankUiState
import com.example.scrambleword.feature.practice.viewmodel.FillBlankViewModel
import kotlin.collections.chunked
import kotlin.collections.forEach
import kotlin.collections.orEmpty
import kotlin.math.sin


@Composable
fun FillBlankScreen(
    onBack: () -> Unit,
    viewModel: FillBlankViewModel = hiltViewModel()
) {


    val uiState by viewModel.uiState.collectAsStateWithLifecycle()


    FillBlankContent(
        uiState,
        onBack = onBack,
        onNext = viewModel::onNext,
        load = viewModel::load,
        onCheck = viewModel::onCheck,
        onInputChange = viewModel::onInputChange,
        onOptionSelected = viewModel::onOptionSelected
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FillBlankContent(
    uiState: FillBlankUiState,
    onBack: () -> Unit,
    onNext: () -> Unit,
    load: () -> Unit,
    onCheck: () -> Unit,
    onInputChange: (String) -> Unit,
    onOptionSelected: (String) -> Unit


) {
    val showBottomBar = !uiState.isLoading &&
            uiState.error == null &&
            !uiState.sessionFinished &&
            uiState.current != null
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            "تمرین",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            "${uiState.level}. ${difficultyLabel(uiState.difficulty)}",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "بازگشت"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        bottomBar = {
            if (showBottomBar) {
                val ex = uiState.current!!
                val checked = uiState.checked
                val isLast = uiState.index + 1 >= uiState.exercises.size
                Surface(
                    tonalElevation = 3.dp,
                    shadowElevation = 8.dp,
                    color = MaterialTheme.colorScheme.background
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .windowInsetsPadding(WindowInsets.navigationBars)
                            .windowInsetsPadding(WindowInsets.ime)
                            .padding(horizontal = MaterialTheme.spacing.default)
                            .padding(
                                top = MaterialTheme.spacing.extraSmall,
                                bottom = MaterialTheme.spacing.small
                            )
                    ) {
                        Button(
                            onClick = {
                                if (checked) onNext()
                                else onCheck()
                            },
                            enabled = checked || uiState.userInput.isNotBlank(),
                            modifier = Modifier
                                .fillMaxWidth()
                                .navigationBarsPadding()
                                .height(52.dp),
                            shape = RoundedCornerShape(50),
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                            contentPadding = PaddingValues()
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    when {
                                        !checked -> "بررسی"
                                        isLast -> "پایان جلسه"
                                        else -> "سؤال بعدی"
                                    },
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.SemiBold

                                )
                            }
                        }
                    }

                }
            }


        }
    ) { padding ->
        when {
            uiState.isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) { CircularProgressIndicator(color = MaterialTheme.colorScheme.primary) }
            }

            uiState.error != null -> {
                ErrorContent(
                    message = uiState.error!!,
                    onRetry = load,
                    modifier = Modifier.padding(padding)
                )

            }

            uiState.sessionFinished -> {
                SessionFinishContent(
                    correct = uiState.correctCount,
                    total = uiState.exercises.size,
                    onBack = onBack,
                    onRetry = load,
                    modifier = Modifier.padding(padding)
                )
            }

            else -> {
                val ex = uiState.current ?: return@Scaffold
                FillBlankQuestionContent(
                    exercise = ex,
                    index = uiState.index,
                    total = uiState.exercises.size,
                    userInput = uiState.userInput,
                    checked = uiState.checked,
                    isCorrect = uiState.isCorrect,
                    onInputChange = onInputChange,
                    onCheck = onCheck,
                    onNext = onNext,
                    onOptionSelected = onOptionSelected,
                    modifier = Modifier.padding(padding)
                )

            }
        }


    }
}

private fun difficultyLabel(d: PracticeDifficulty): String = when (d) {
    PracticeDifficulty.EASY -> "آسان"
    PracticeDifficulty.NORMAL -> "عادی"
    PracticeDifficulty.HARD -> "سخت"
}

@Composable
fun ErrorContent(
    message: String,
    onRetry: () -> Unit,
    modifier: Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "مشکلی پیش آمد",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
        Button(
            onClick = onRetry,
            shape = RoundedCornerShape(50),
            modifier = Modifier.height(48.dp)
        ) {
            Text("تلاش دوباره")
        }
    }

}

@Composable
fun SessionFinishContent(
    correct: Int,
    total: Int,
    onBack: () -> Unit,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            "پایان تمرین",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.height(12.dp))
        Button(
            onClick = onRetry,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(50)
        ) {
            Text("دوباره")
        }
        Spacer(Modifier.height(10.dp))
        OutlinedButton(
            onClick = onBack,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(50)
        ) {
            Text("بازگشت")
        }
    }


}


@Composable
private fun FillBlankQuestionContent(
    exercise: FillBlankExerciseDto,
    index: Int,
    total: Int,
    userInput: String,
    checked: Boolean,
    isCorrect: Boolean?,
    onInputChange: (String) -> Unit,
    onCheck: () -> Unit,
    onNext: () -> Unit,
    onOptionSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val progress = (index + 1).toFloat() / total.coerceAtLeast(1)
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp)
            .padding(top = 8.dp, bottom = 16.dp)
    ) {
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "${index + 1} از $total",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
            ) {
                Text(
                    exercise.level,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
        Spacer(Modifier.height(8.dp))
        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(50)),
            color = MaterialTheme.colorScheme.primary,
            trackColor = MaterialTheme.colorScheme.surfaceVariant
        )
        Spacer(Modifier.height(20.dp))
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(Modifier.padding(20.dp)) {
                Text(
                    exercise.prompt,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(Modifier.height(16.dp))

                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                    Text(
                        exercise.stem,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface,
                        lineHeight = 34.sp
                    )
                }
                Spacer(Modifier.height(12.dp))
                Text(
                    exercise.translationFa,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(Modifier.height(12.dp))
                when (exercise.mode) {
                    FillBlankMode.CHOICE -> {
                        ChoiceOptions(
                            options = exercise.options.orEmpty(),
                            answer = exercise.answer,
                            userInput = userInput,
                            checked = checked,
                            isCorrect = isCorrect,
                            onOptionSelected = onOptionSelected
                        )
                    }

                    FillBlankMode.SCRAMBLE, FillBlankMode.TYPE -> {
                        if (exercise.mode == FillBlankMode.SCRAMBLE &&
                            !exercise.scrambleHint.isNullOrBlank()
                        ) {
                            Text(
                                ":حروف${exercise.scrambleHint}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(Modifier.height(10.dp))
                        }
                        OutlinedTextField(
                            value = userInput,
                            onValueChange = onInputChange,
                            modifier = Modifier.fillMaxWidth(),
                            enabled = !checked,
                            singleLine = true,
                            label = { Text("پاسح شما") },
                            placeholder = { Text("") },
                            shape = RoundedCornerShape(16.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = MaterialTheme.colorScheme.primary,
                                unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
                            )
                        )
                    }
                }

                AnimatedVisibility(visible = checked) {
                    val ok = isCorrect == true

                    Text(
                        text = if (ok) "آفرین درست بود!" else ":درست${exercise.answer}",
                        color = if (ok) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }

    }
}


@Composable
private fun ChoiceOptions(
    options: List<String>,
    answer: String,
    userInput: String,
    checked: Boolean,
    isCorrect: Boolean?,
    onOptionSelected: (String) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        options.chunked(2).forEach { rowItems ->
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                rowItems.forEach { option ->
                    val selected = userInput.equals(option, ignoreCase = true)
                    OutlinedButton(
                        onClick = { if (!checked) onOptionSelected(option) },
                        enabled = !checked,
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                        shape = RoundedCornerShape(14.dp),
                        border = BorderStroke(
                            width = if (selected) 2.dp else 1.dp,
                            color = when {
                                checked && option.equals(answer, true) ->
                                    MaterialTheme.colorScheme.primary

                                checked && selected && isCorrect == false ->
                                    MaterialTheme.colorScheme.error

                                selected -> MaterialTheme.colorScheme.primary
                                else -> MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
                            }
                        ),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = if (selected)
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
                            else MaterialTheme.colorScheme.surface
                        )
                    ) {
                        Text(option, maxLines = 1)
                    }
                }
                if (rowItems.size == 1) Spacer(Modifier.weight(1f))
            }
        }
    }
}


@Composable
private fun MetaChip(text: String) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f)
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}


@Preview(showBackground = true, locale = "fa")
@Composable
fun previewFillBlankScreen() {
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        WordScrambleTheme(darkTheme = true) {
            FillBlankContent(
                load = {},
                onInputChange = {},
                onNext = {},
                onBack = {},
                onCheck = {},
                onOptionSelected = {},
                uiState = FillBlankUiState()
            )
        }
    }
}