package com.example.scrambleword.feature.wordlist.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.util.query
import com.example.scrambleword.domain.model.Language
import com.example.scrambleword.domain.model.Word
import com.example.scrambleword.domain.usecase.word.ObserveWordsUseCase
import com.example.scrambleword.domain.usecase.word.SearchWordUseCase
import com.example.scrambleword.feature.wordlist.toListItem
import com.example.scrambleword.feature.wordlist.uistate.WordListEmptyReason
import com.example.scrambleword.feature.wordlist.uistate.WordListUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.annotation.meta.When
import javax.inject.Inject


@HiltViewModel
class WordListViewModel @Inject constructor(
    private val observeWordsUseCase: ObserveWordsUseCase,
    private val searchWordsUseCase: SearchWordUseCase
) : ViewModel() {


    private val searchQuery = MutableStateFlow("")
    private val selectedLanguage = MutableStateFlow<Language?>(null)



    @OptIn(ExperimentalCoroutinesApi::class)
    val uiState: StateFlow<WordListUiState> =
        combine(
            searchQuery,
            selectedLanguage,
        ) { query, language ->
            query to language
        }.flatMapLatest { (query, language) ->
            android.util.Log.d("WordList", "filter language=${language?.code} query=$query")
            val wordsFlow: Flow<List<Word>> =
                if (query.isBlank()) {
                    observeWordsUseCase(language)
                } else {
                    searchWordsUseCase(query).map { list ->
                        if (language == null) list
                        else list.filter { it.language == language }
                    }
                }
            combine(
                wordsFlow, observeWordsUseCase(null)
            ) { words, allWords ->
                android.util.Log.d("WordList", "words=${words.size} all=${allWords.size}")
                val reason = when {
                    words.isNotEmpty() -> WordListEmptyReason.NONE
                    allWords.isEmpty() -> WordListEmptyReason.NO_WORDS_AT_ALL
                    query.isNotBlank() -> WordListEmptyReason.NO_SEARCH_RESULT
                    language != null -> WordListEmptyReason.NO_WORDS_FOR_LANGUAGE
                    else -> WordListEmptyReason.NO_WORDS_AT_ALL
                }
                WordListUiState(
                    searchQuery = query,
                    selectedLanguage = language,
                    items = words.toListItem(),
                    emptyReason = reason,
                    totalWordsCount = allWords.size,
                    isEmpty = words.isEmpty()
                )
            }

        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = WordListUiState()
        )


    fun onSearchQueryChange(value: String) {
        searchQuery.value = value
    }

    fun onLanguageSelected(language: Language?) {
        android.util.Log.d("WordList", "onLanguageSelected: ${language?.code}")
        selectedLanguage.value = language
    }

}