package com.example.scrambleword.data.remote.exercise



enum class FillBlankMode {
    TYPE,
    CHOICE,
    SCRAMBLE
}


data class FillBlankExerciseDto(
    val exerciseId: String,
    val type: String = "FILL_BLANK",
    val mode: FillBlankMode = FillBlankMode.TYPE,
    val level: String,
    val scenario: String,
    val sourceSentenceId: String,
    val prompt: String,
    val stem: String,
    val answer: String,
    val acceptableAnswers: List<String> = emptyList(),
    val options: List<String>?=null,
    val scrambleHint: String?=null,
    val translationFa: String,
    val hint: String? = null,
    val targets: FillBlankTargetsDto? = null
)
