package com.example.scrambleword.feature.lesson

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.scrambleword.data.remote.exercise.FillBlankExerciseDto
import com.example.scrambleword.data.remote.exercise.FillBlankMode
import com.example.scrambleword.data.remote.exercise.LessonStepDto


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LessonScreen(
    onBack: () -> Unit,
    viewModel: LessonViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val showBottomBar =
        uiState.phase == LessonPhase.LEARNING || uiState.phase == LessonPhase.PRACTICING


    Scaffold(
        containerColor = colorScheme.background,
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            uiState.titleFa.ifBlank { "درس" },
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                        if (uiState.totalSteps > 0 &&
                            uiState.phase != LessonPhase.LESSON_DONE
                        ) {
                            Text(
                                "قدم ${uiState.stepNumber} از ${uiState.totalSteps}",
                                style = MaterialTheme.typography.labelMedium,
                                color = colorScheme.onSurfaceVariant
                            )
                        }
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
                    containerColor = colorScheme.background
                )
            )
        },
        bottomBar = {
            if (showBottomBar) {
                LessonBottomBar(
                    phase = uiState.phase,
                    checked = uiState.checked,
                    isLastStep = uiState.isLastStep,
                    canSubmit = uiState.userInput.isNotBlank(),
                    onContinueFromLearn = viewModel::onContinueFromLearn,
                    onCheck = viewModel::onCheck,
                    onNextStep = viewModel::onNextStep
                )
            }
        }
    ) { padding ->
        when (uiState.phase) {
            LessonPhase.LOADING -> {
                Box(
                    Modifier.fillMaxSize().padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            LessonPhase.ERROR -> {
                Column(
                    Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(uiState.error ?: "خطا")
                    Spacer(Modifier.height(16.dp))
                    Button(onClick = viewModel::load) { Text("تلاش دوباره") }
                }
            }

            LessonPhase.LEARNING -> {
                val step = uiState.currentStep
                if (step != null) {
                    LessonLearnContent(
                        step = step,
                        stepNumber = uiState.stepNumber,
                        totalSteps = uiState.totalSteps,
                        modifier = Modifier
                            .padding(padding)
                            .verticalScroll(rememberScrollState())
                    )
                }
            }

            LessonPhase.PRACTICING -> {
                val exercise = uiState.exercise
                if (exercise != null) {
                    LessonPracticeContent(
                        exercise = exercise,
                        userInput = uiState.userInput,
                        checked = uiState.checked,
                        isCorrect = uiState.isCorrect,
                        onInputChange = viewModel::onInputChange,
                        onOptionSelected = viewModel::onOptionSelected,
                        modifier = Modifier
                            .padding(padding)
                            .verticalScroll(rememberScrollState())
                    )
                }
            }

            LessonPhase.LESSON_DONE -> {
                Column(
                    Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        "درس تمام شد",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.height(12.dp))
                    Text(
                        "${uiState.correctCount} از ${uiState.totalSteps} درست",
                        style = MaterialTheme.typography.titleLarge,
                        color = colorScheme.primary,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(Modifier.height(28.dp))
                    Button(
                        onClick = viewModel::onRetryLesson,
                        modifier = Modifier.fillMaxWidth().height(48.dp),
                        shape = RoundedCornerShape(50)
                    ) { Text("دوباره همین درس") }
                    Spacer(Modifier.height(10.dp))
                    OutlinedButton(
                        onClick = onBack,
                        modifier = Modifier.fillMaxWidth().height(48.dp),
                        shape = RoundedCornerShape(50)
                    ) { Text("بازگشت") }
                }
            }
        }
    }
}

@Composable
private fun LessonBottomBar(
    phase: LessonPhase,
    checked: Boolean,
    isLastStep: Boolean,
    canSubmit: Boolean,
    onContinueFromLearn: () -> Unit,
    onCheck: () -> Unit,
    onNextStep: () -> Unit
) {
    Surface(tonalElevation = 3.dp, shadowElevation = 8.dp) {
        val (label, enabled, onClick) = when {
            phase == LessonPhase.LEARNING ->
                Triple("برو به تمرین", true, onContinueFromLearn)
            phase == LessonPhase.PRACTICING && !checked ->
                Triple("بررسی", canSubmit, onCheck)
            phase == LessonPhase.PRACTICING && checked ->
                Triple(
                    if (isLastStep) "پایان درس" else "قدم بعدی",
                    true,
                    onNextStep
                )
            else -> Triple("", false, {})
        }

        Button (
            onClick = onClick,
            enabled = enabled,
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .imePadding()
                .padding(horizontal = 20.dp, vertical = 12.dp)
                .height(52.dp),
            shape = RoundedCornerShape(50)
        ) {
            Text(
                label,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
private fun LessonLearnContent(
    step: LessonStepDto,
    stepNumber: Int,
    totalSteps: Int,
    modifier: Modifier = Modifier
) {
    val colorScheme = MaterialTheme.colorScheme
    val progress = stepNumber.toFloat() / totalSteps.coerceAtLeast(1)

    Column(
        modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .padding(top = 8.dp, bottom = 16.dp)
    ) {
        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(50)),
            color = colorScheme.primary,
            trackColor = colorScheme.surfaceVariant
        )
        Spacer(Modifier.height(20.dp))

        Text(
            "یاد بگیر",
            style = MaterialTheme.typography.labelLarge,
            color = colorScheme.primary,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(Modifier.height(12.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(Modifier.padding(20.dp)) {
                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                    Text(
                        step.learnText,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.SemiBold,
                        lineHeight = 34.sp
                    )
                }
                Spacer(Modifier.height(12.dp))
                Text(
                    step.translationFa,
                    style = MaterialTheme.typography.bodyLarge,
                    color = colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(Modifier.height(16.dp))

        Surface(
            shape = RoundedCornerShape(16.dp),
            color = colorScheme.primary.copy(alpha = 0.10f),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(Modifier.padding(16.dp)) {
                Text(
                    "نکته",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = colorScheme.primary
                )
                Spacer(Modifier.height(6.dp))
                Text(
                    step.tipFa,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Composable
private fun LessonPracticeContent(
    exercise: FillBlankExerciseDto,
    userInput: String,
    checked: Boolean,
    isCorrect: Boolean?,
    onInputChange: (String) -> Unit,
    onOptionSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val colorScheme = MaterialTheme.colorScheme

    Column(
        modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .padding(top = 8.dp, bottom = 16.dp)
    ) {
        Text(
            "تمرین کن",
            style = MaterialTheme.typography.labelLarge,
            color = colorScheme.primary,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(Modifier.height(12.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = colorScheme.surface)
        ) {
            Column(Modifier.padding(20.dp)) {
                Text(
                    exercise.prompt,
                    style = MaterialTheme.typography.bodyMedium,
                    color = colorScheme.onSurfaceVariant
                )
                Spacer(Modifier.height(12.dp))
                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                    Text(
                        exercise.stem,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                Spacer(Modifier.height(8.dp))
                Text(
                    exercise.translationFa,
                    style = MaterialTheme.typography.bodyMedium,
                    color = colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(Modifier.height(20.dp))

        when (exercise.mode) {
            FillBlankMode.CHOICE -> {
                val options = exercise.options.orEmpty()
                Column (verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    options.chunked(2).forEach { row ->
                        Row (
                            Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            row.forEach { option ->
                                val selected = userInput.equals(option, ignoreCase = true)
                                OutlinedButton(
                                    onClick = { if (!checked) onOptionSelected(option) },
                                    enabled = !checked,
                                    modifier = Modifier.weight(1f).height(48.dp),
                                    shape = RoundedCornerShape(14.dp),
                                    border = BorderStroke(
                                        if (selected) 2.dp else 1.dp,
                                        when {
                                            checked && option.equals(exercise.answer, true) ->
                                                colorScheme.primary
                                            checked && selected && isCorrect == false ->
                                                colorScheme.error
                                            selected -> colorScheme.primary
                                            else -> colorScheme.outline.copy(alpha = 0.5f)
                                        }
                                    )
                                ) { Text(option, maxLines = 1) }
                            }
                            if (row.size == 1) Spacer(Modifier.weight(1f))
                        }
                    }
                }
            }

            FillBlankMode.SCRAMBLE, FillBlankMode.TYPE -> {
                if (exercise.mode == FillBlankMode.SCRAMBLE &&
                    !exercise.scrambleHint.isNullOrBlank()
                ) {
                    Text(
                        "حروف: ${exercise.scrambleHint}",
                        color = colorScheme.onSurfaceVariant
                    )
                    Spacer(Modifier.height(10.dp))
                }
                OutlinedTextField(
                    value = userInput,
                    onValueChange = onInputChange,
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !checked,
                    singleLine = true,
                    label = { Text("پاسخ") },
                    shape = RoundedCornerShape(16.dp)
                )
            }
        }

        if (checked) {
            Spacer(Modifier.height(14.dp))
            Text(
                if (isCorrect == true) "آفرین، درست بود!"
                else "درست: ${exercise.answer}",
                color = if (isCorrect == true) colorScheme.primary else colorScheme.error,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

