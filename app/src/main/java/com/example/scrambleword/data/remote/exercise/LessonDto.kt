package com.example.scrambleword.data.remote.exercise

import com.google.gson.annotations.SerializedName


data class LessonDto(
    val id: String,
    val scenario: String,
    val language: String,
    val level: String,
    @SerializedName("title_fa") val titleFa: String,
    @SerializedName("title_en") val titleEn: String? = null,
    @SerializedName("description_fa") val descriptionFa: String? = null,
    val version: String? = null,
    @SerializedName("step_count") val stepCount: Int = 0,
    val steps: List<LessonStepDto> = emptyList()
)
