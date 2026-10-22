package com.example.scrambleword.domain.model.practice

enum class SlotsStroke {
    Empty,
    Correct,
    Wrong
}

fun PracticeSlot.stroke(cubes: List<CubeLetter>): SlotsStroke {
    val placedId = cubeId ?: return SlotsStroke.Empty
    val cube = cubes.find { it.id == placedId } ?: return SlotsStroke.Empty
    return if (cube.char.equals(expected, ignoreCase = true)) {
        SlotsStroke.Correct
    } else {
        SlotsStroke.Wrong
    }
}
