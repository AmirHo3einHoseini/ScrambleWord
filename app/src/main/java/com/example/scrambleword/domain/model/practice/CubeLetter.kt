package com.example.scrambleword.domain.model.practice

data class CubeLetter(
    val id: Int,
    val char: Char,
    val homeIndex: Int,
    val isInBank: Boolean = true
)