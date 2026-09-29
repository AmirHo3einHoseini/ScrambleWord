package com.example.scrambleword.domain.model

data class DictionaryInfo(
    val word: String,
    val phonetic: String? = null,
    val audioUrl: String? = null,
    val partOfSpeech: String? = null,
    val examples: List<String> = emptyList(),
    val definitions: List<String> = emptyList(),
    val translationsFa: List<String> = emptyList(),
)
