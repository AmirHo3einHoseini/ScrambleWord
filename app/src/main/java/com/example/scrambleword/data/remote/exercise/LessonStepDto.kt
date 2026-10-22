package com.example.scrambleword.data.remote.exercise

import com.google.gson.annotations.SerializedName

data class LessonStepDto (
    val id: String,
    val order: Int,
    @SerializedName("sentence_id") val sentenceId: String,
    @SerializedName("learn_text") val learnText: String,
    @SerializedName("translation_fa") val translationFa: String,
    @SerializedName("tip_fa") val tipFa: String,
    @SerializedName("tip_en") val tipEn: String? = null,
    @SerializedName("focus_lemma") val focusLemma: String,
    @SerializedName("focus_surface") val focusSurface: String,
    @SerializedName("grammar_focus") val grammarFocus: String? = null,
    @SerializedName("practice_mode") val practiceMode: String = "FILL_BLANK"
)