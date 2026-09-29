package com.example.scrambleword.data.remote.dto

data class WiktSenseDto(
    val glosses: List<String> = emptyList(),
    val examples: List<WiktExampleDto> = emptyList()
)

