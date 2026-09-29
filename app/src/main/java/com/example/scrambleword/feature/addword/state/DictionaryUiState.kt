package com.example.scrambleword.feature.addword.state

import com.example.scrambleword.domain.model.DictionaryInfo
import com.example.scrambleword.domain.model.Language


data class DictionaryUiState(
    val wordInput: String="",
    val selectedLanguage: Language = Language.ENGLISH,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val result: DictionaryInfo? = null,
    val selectedTranslation: String? = null,
    val selectedExample: String? = null
)