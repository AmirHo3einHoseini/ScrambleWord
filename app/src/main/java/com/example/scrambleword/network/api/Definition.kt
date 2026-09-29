package com.example.scrambleword.network.api

data class Definition(
    val definition: String,
    val example: String? =null,
    val synonyms: List<String> =emptyList(),
    val antonyms : List<String> =emptyList()
)
