package com.example.scrambleword.data.remote.dto

data class FreeDictionaryEntryDto(
    val word: String? = null,
    val phonetic: String? = null,
    val phonetics: List<FreePhoneticDto> =emptyList(),
    val meanings: List<FreeMeaningDto> = emptyList()
)
