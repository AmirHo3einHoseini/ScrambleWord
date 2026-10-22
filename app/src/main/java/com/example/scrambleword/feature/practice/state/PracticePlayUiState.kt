package com.example.scrambleword.feature.practice.state

import com.example.scrambleword.domain.model.practice.CubeLetter
import com.example.scrambleword.domain.model.practice.PracticeDifficulty
import com.example.scrambleword.domain.model.practice.PracticeSlot

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
    val isSessionFinished: Boolean = false,
    val difficulty: PracticeDifficulty = PracticeDifficulty.NORMAL,
    val sessionScore: Int = 0,
    val lastWordPoints: Int? = null,
    val perfectCount: Int = 0,
    val perfectStreak: Int = 0,
    val showHint: Boolean = false,
    val hintButtonVisible: Boolean = false,
    val canUseHint: Boolean = false,
    val wordMistakeCount: Int = 0,
    val wordUsedHint: Boolean = false
)