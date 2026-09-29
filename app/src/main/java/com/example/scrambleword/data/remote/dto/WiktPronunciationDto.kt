package com.example.scrambleword.data.remote.dto

data class WiktPronunciationDto(
    val ipa: String? = null,
    val text: String? = null,
    val tags: List<String> = emptyList()
)
