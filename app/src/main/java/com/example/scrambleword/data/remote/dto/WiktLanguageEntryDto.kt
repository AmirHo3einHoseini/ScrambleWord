package com.example.scrambleword.data.remote.dto

data class WiktLanguageEntryDto(
    val lang: String? = null,
    val langCode: String? = null,
    val senses: List<WiktSenseDto> = emptyList(),
    val pronunciations: List<WiktPronunciationDto> = emptyList(),
    val partOfSpeech: String? = null,
    val pos: String? = null
)
