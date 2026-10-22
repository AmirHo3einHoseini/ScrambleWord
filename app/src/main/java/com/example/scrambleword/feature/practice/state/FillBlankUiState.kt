package com.example.scrambleword.feature.practice.state

import com.example.scrambleword.data.remote.exercise.FillBlankExerciseDto
import com.example.scrambleword.domain.model.practice.PracticeDifficulty

data class FillBlankUiState(
    val isLoading: Boolean = true,
    val error: String? = null,
    val exercises: List<FillBlankExerciseDto> = emptyList(),
    val index: Int = 0,
    val userInput: String = "",
    val checked: Boolean = false,
    val isCorrect: Boolean? = null,
    val sessionFinished: Boolean = false,
    val correctCount: Int = 0,
    val level: String = "A1",
    val difficulty: PracticeDifficulty = PracticeDifficulty.EASY
){
    val current: FillBlankExerciseDto? get() = exercises.getOrNull(index)
}
