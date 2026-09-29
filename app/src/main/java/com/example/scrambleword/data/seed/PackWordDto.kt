package com.example.scrambleword.data.seed

import com.example.scrambleword.domain.model.Language
import com.google.gson.annotations.SerializedName

data class PackWordDto(
    val word: String? =null,
    val language: String? =null,
    val level: String? = null,
    val category: String? = null,
    val pos: String? = null,
    @SerializedName("meaning_fa") val meaningFa: String? = null,
    @SerializedName("example_en") val exampleEn: String? = null,
    @SerializedName("example_fa") val exampleFa: String? = null,
    val phonetic: String? = null,
    val hint: String? = null,
    @SerializedName("audio_url") val audioUrl: String? = null,
    @SerializedName("quality_status") val qualityStatus: String? = null
)
