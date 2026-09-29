package com.example.scrambleword.feature.addword.state

import com.example.scrambleword.domain.model.Language
import com.example.scrambleword.network.api.DictionaryResponse

data class AddWordUiState(
    val wordInput: String = "",
    val meaningInput: String = "",
    val hintInput: String = "",
    val selectedLanguage: Language = Language.ENGLISH,
    val selectedCategory: String = "عمومی",
    val isSaving: Boolean = false,
    val dictionaryResponse: DictionaryResponse? = null,
    val errorMessage: String? = null,
    val isSaved: Boolean = false,
    val selectedPartOfSpeech: String? = null,
    val selectedExamples: List<String> = emptyList(),
    val selectedDefinitions: List<String> = emptyList(),
    val showSavedMessage: Boolean = false,
    val phonetic: String? = null,
    val audioUrl: String? = null,
    val hasExtraDetails: Boolean = false,
    val exampleFromDictionary: List<String> = emptyList()


)
