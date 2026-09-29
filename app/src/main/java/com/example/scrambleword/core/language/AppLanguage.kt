package com.example.scrambleword.core.language

import androidx.compose.ui.unit.LayoutDirection
import java.util.Locale

enum class AppLanguage(
    val code: String,
    val displayName: String,
    val locale: Locale,
    val layoutDirection: LayoutDirection
) {
    ENGLISH(
        code = "en",
        displayName = "English",
        locale = Locale.ENGLISH,
        layoutDirection = LayoutDirection.Ltr
    ),
    PERSIAN(
        code = "fa",
        displayName = "فارسی",
        locale = Locale("fa"),
        layoutDirection = LayoutDirection.Rtl
    );

    companion object {
        fun fromCode(code: String): AppLanguage {
            return entries.find { it.code.equals(code, ignoreCase = true) } ?: ENGLISH
        }
    }
}