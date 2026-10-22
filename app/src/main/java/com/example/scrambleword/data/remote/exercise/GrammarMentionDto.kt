package com.example.scrambleword.data.remote.exercise

data class GrammarMentionDto(
    val conceptName: String,
    val category: String? = null,
    val isPrimaryTarget: Boolean = true,
    val evidenceText: String? = null
)
