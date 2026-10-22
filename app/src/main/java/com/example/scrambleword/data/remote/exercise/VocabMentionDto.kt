package com.example.scrambleword.data.remote.exercise

data class VocabMentionDto(
    val surface: String,
    val lemma: String,
    val language: String = "en",
    val pos: String? = null,
    val grammaticalRole: String? = null,
    val cefrEstimate: String? = null,
    val isTarget: Boolean = true
)
