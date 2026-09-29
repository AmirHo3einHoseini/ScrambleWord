package com.example.scrambleword.domain.model

data class PracticeConfig(
    val language: Language,
    val category: String? = null,
    val mode:PracticeMode = PracticeMode.SPELLING,
    val wordCount: Int = 10
)