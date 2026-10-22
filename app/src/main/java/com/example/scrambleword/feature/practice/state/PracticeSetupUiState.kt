package com.example.scrambleword.feature.practice.state

import com.example.scrambleword.domain.model.CategoryCount
import com.example.scrambleword.domain.model.Language
import com.example.scrambleword.domain.model.practice.PracticeDifficulty

data class PracticeSetupUiState(
    val selectedLanguage: Language = Language.ENGLISH,
    val selectedCategory: String? = null,
    val categoryCounts: List<CategoryCount> = emptyList(),
    val totalCount: Int = 0,
    val selectedCount: Int = 0,
    val canStart: Boolean = false,
    val selectedDifficulty: PracticeDifficulty = PracticeDifficulty.NORMAL
)