package com.example.scrambleword.domain.model.practice

data class PracticeSlot(
    val index: Int,
    val expected: Char,
    val cubeId: Int? =null
)