package com.example.scrambleword.domain.model.practice

import com.example.scrambleword.domain.model.Word

data class PracticeQuestion(
    val word: Word,
    val cubes: List<CubeLetter>,
    val slots: List<PracticeSlot>
)

fun Word.toPracticeQuestion(): PracticeQuestion {
    val normalized = word.trim()
    require(normalized.isNotEmpty()) { "Word for practice cannot be blank" }

    val chars = normalized.toList()

    // id = موقعیت اصلی در کلمه (برای حروف تکراری یکتا می‌ماند)
    val withOriginIndex = chars.mapIndexed { originIndex, char ->
        originIndex to char
    }
    val shuffled = withOriginIndex.shuffled()

    val cubes = shuffled.mapIndexed { homeIndex, (originIndex, char) ->
        CubeLetter(
            id = originIndex,
            char = char,
            homeIndex = homeIndex,
            isInBank = true
        )
    }
    val slots = chars.mapIndexed { index, char ->
        PracticeSlot(
            index = index,
            expected = char,
            cubeId = null
        )
    }
    return PracticeQuestion(
        word = this,
        cubes = cubes,
        slots = slots
    )
}