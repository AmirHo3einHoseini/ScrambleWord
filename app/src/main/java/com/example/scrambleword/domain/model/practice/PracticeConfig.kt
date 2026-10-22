package com.example.scrambleword.domain.model.practice

import com.example.scrambleword.domain.model.Language

data class PracticeConfig(
    val language: Language,
    val category: String? = null,
    val mode: PracticeMode = PracticeMode.SPELLING,
    val wordCount: Int = 10,
    val difficulty: PracticeDifficulty = PracticeDifficulty.NORMAL
)