package com.example.scrambleword.data.remote.dto

data class FreeMeaningDto(
    val partOfSpeech: String? = null,
    val definitions: List<FreeDefinitionDto> = emptyList()
)
