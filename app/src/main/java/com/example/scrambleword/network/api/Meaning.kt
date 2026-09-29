package com.example.scrambleword.network.api

data class Meaning(
    val partOfSpeech: String? = null,
    val definition: List<Definition> = emptyList(),
    val synonyms: List<String> = emptyList(),
    val antonyms: List<String> = emptyList()
)
