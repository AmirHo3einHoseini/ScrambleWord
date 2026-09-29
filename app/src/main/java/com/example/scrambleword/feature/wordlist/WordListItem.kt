package com.example.scrambleword.feature.wordlist

import com.example.scrambleword.domain.model.Language
import com.example.scrambleword.domain.model.Word

sealed class WordListItem {

    data class LanguageHeader(
        val language: Language,
        val count: Int,
    ) : WordListItem()

    data class WordRow(
        val word: Word
    ) : WordListItem()
}

fun List<Word>.toListItem(): List<WordListItem> {
    if (isEmpty()) return emptyList()
    val result = mutableListOf<WordListItem>()

    groupBy { it.language }.forEach { language, words ->
        result += WordListItem.LanguageHeader(
            language = language,
            count = words.size
        )
        words.forEach { result += WordListItem.WordRow(it) }
    }

    return result
}