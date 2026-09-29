package com.example.scrambleword.domain.model

data class Word(
    val id: Long = 0,
    val word: String,
    val meaning: String,
    val language: Language,
    val category: String,
    val phonetic: String? = null,
    val audioUrl: String? = null,
    val partOfSpeech: String? = null,
    val example: List<String>? = null,
    val synonyms: List<String>? = null,
    val definitions: List<String>? = null,
    val hint: String? = null,
    val level: Int = 1,
    val isFavorite: Boolean = false,
    val correctCount: Int = 0,
    val wrongCount: Int = 0,
    val lastPracticed: Long? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),

)
