package com.example.scrambleword.feature.lesson

import com.example.scrambleword.data.remote.exercise.FillBlankExerciseDto
import com.example.scrambleword.data.remote.exercise.LessonStepDto
import com.example.scrambleword.domain.model.practice.PracticeDifficulty

data class LessonUiState(
    val phase: LessonPhase = LessonPhase.LOADING,
    val error: String? = null,
    val lessonId: String = "",
    val titleFa: String = "",
    val level: String = "A1",

    val steps: List<LessonStepDto> = emptyList(),
    val stepIndex: Int = 0,

    // تمرین قدم فعلی
    val exercise: FillBlankExerciseDto? = null,
    val userInput: String = "",
    val checked: Boolean = false,
    val isCorrect: Boolean? = null,

    val correctCount: Int = 0,
    val difficulty: PracticeDifficulty = PracticeDifficulty.EASY
){
    val currentStep: LessonStepDto? get() = steps.getOrNull(stepIndex)
    val stepNumber: Int get() = stepIndex + 1
    val totalSteps: Int get() = steps.size
    val isLastStep: Boolean get() = stepIndex >= steps.lastIndex
}
