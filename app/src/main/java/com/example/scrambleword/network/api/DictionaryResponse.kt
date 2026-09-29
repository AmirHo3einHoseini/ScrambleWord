package com.example.scrambleword.network.api

data class DictionaryResponse(
    val word: String,
    val phonetic: String? = null,
    val phonetics: List<Phonetic> = emptyList(),
    val meanings: List<Meaning> = emptyList(),
    val sourceUrl: List<String> = emptyList()
)
