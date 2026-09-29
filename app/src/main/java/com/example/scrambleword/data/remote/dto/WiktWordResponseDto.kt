package com.example.scrambleword.data.remote.dto

data class WiktWordResponseDto(
    val word: String? = null,
    val edition: String? = null,
    val entries: List<WiktEntryDto> = emptyList()
)
