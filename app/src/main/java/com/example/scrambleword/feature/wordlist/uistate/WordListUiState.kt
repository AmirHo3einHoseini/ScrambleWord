package com.example.scrambleword.feature.wordlist.uistate

import com.example.scrambleword.domain.model.Language
import com.example.scrambleword.feature.wordlist.WordListItem

data class WordListUiState(
    val searchQuery: String = "",
    val selectedLanguage: Language? = null,
    val items: List<WordListItem> = emptyList(),
    val emptyReason: WordListEmptyReason = WordListEmptyReason.NO_WORDS_AT_ALL,
    val totalWordsCount: Int = 0,
    val isEmpty: Boolean = true
)


