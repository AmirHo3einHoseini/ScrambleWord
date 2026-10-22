package com.example.scrambleword.domain.model.practice

enum class PracticeDifficulty {
    EASY,
    NORMAL,
    HARD
}


fun PracticeDifficulty.baseScore(): Int = when (this) {
    PracticeDifficulty.EASY -> 10
    PracticeDifficulty.NORMAL -> 20
    PracticeDifficulty.HARD -> 30
}

fun PracticeDifficulty.undoPenalty(): Int = when (this) {
    PracticeDifficulty.EASY -> 0
    PracticeDifficulty.NORMAL -> 1
    PracticeDifficulty.HARD -> 2
}