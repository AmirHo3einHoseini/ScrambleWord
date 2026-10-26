package com.example.scrambleword.data.remote.exercise

import com.google.gson.annotations.SerializedName

data class ScenarioSentenceDto(
    val id: String?,
    val scenario: String,
    val language: String,
    val level: String?,
    val step: Int,
    @SerializedName("step_name")
    val stepName: String,
    val speaker: String,
    val function: String,
    val text: String?,
    @SerializedName("translation_fa")
    val translationFa: String,

    @SerializedName("next_id")
    val nextId: String? = null,

    @SerializedName("vocab_mentions")
    val vocabMentions: List<VocabMentionDto> = emptyList(),

    @SerializedName("grammar_mentions")
    val grammarMentions: List<GrammarMentionDto> = emptyList()
)
