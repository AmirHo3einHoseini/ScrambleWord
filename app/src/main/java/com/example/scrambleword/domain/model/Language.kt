package com.example.scrambleword.domain.model

enum class Language(
    val code: String,
    val displayName: String,
    val flag: String
) {
    ENGLISH("en", "English","🇬🇧"),
    GERMAN("de","Deutsch" ,"🇩🇪"),
    FRENCH("fr","France" ,"🇫🇷"),
    SPANISH("es", "Spanish","🇪🇸");

    companion object {
        fun fromCode(code: String): Language? {
            return entries.find { it.code.equals(code, ignoreCase = true) }
        }
    }

}