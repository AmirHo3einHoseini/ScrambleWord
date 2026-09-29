package com.example.scrambleword.feature.practice.state

import com.example.scrambleword.domain.model.CubeLetter
import com.example.scrambleword.domain.model.PracticeSlot

data class PracticePlayUiState(
    val isLoading: Boolean = true,
    val errorMessage: String? = null,
    val currentIndex: Int = 0,
    val totalCount: Int = 0,
    val targetWord: String = "",
    val meaning: String = "",
    val hint: String? = null,
    val phonetic: String? = null,
    val audioUrl: String? = null,
    val cubes: List<CubeLetter> = emptyList(),
    val slots: List<PracticeSlot> = emptyList(),
    val isCompleted: Boolean = false,
    val isSessionFinished: Boolean = false
)