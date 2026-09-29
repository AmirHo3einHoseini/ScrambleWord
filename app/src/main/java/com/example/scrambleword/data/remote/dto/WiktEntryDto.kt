package com.example.scrambleword.data.remote.dto

data class WiktEntryDto(
    val word: String? = null,
    val lang: String? = null,
    val lang_code: String? = null,
    val pos: String? = null,
    val senses: List<WiktSenseDto> = emptyList(),
    val sounds: List<WiktSoundDto> = emptyList(),
    val translations: List<WiktTranslationDto> = emptyList()
)
