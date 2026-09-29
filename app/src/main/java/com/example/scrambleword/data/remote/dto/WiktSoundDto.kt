package com.example.scrambleword.data.remote.dto

data class WiktSoundDto(
    val ipa: String? = null,
    val audio: String? = null,
    val mp3_url: String? = null,
    val ogg_url: String? = null
)
