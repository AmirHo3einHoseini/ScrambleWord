package com.example.scrambleword.data.remote.exercise

import com.google.gson.annotations.SerializedName

data class VocabMentionDto(
    val surface: String,
    val lemma: String,
    val language: String = "en",
    val pos: String? = null,
    @SerializedName("grammatical_role")
    val grammaticalRole: String? = null,
    @SerializedName("cefr_estimate")
    val cefrEstimate: String? = null,
    @SerializedName("is_target")
    val isTarget: Boolean = true
)
