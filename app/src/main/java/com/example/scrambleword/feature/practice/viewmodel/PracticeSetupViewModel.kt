package com.example.scrambleword.feature.practice.viewmodel

import android.R.attr.category
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scrambleword.domain.model.Language
import com.example.scrambleword.domain.model.PracticeConfig
import com.example.scrambleword.domain.model.PracticeMode
import com.example.scrambleword.domain.usecase.practice.ObserveCategoryCountsUseCase
import com.example.scrambleword.domain.usecase.practice.ObserveWordCountUseCase
import com.example.scrambleword.feature.practice.state.PracticeSetupUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class PracticeSetupViewModel @Inject constructor(
    private val observeCategoryCounts: ObserveCategoryCountsUseCase,
    private val observeWordCount: ObserveWordCountUseCase
) : ViewModel() {
    private val selectedLanguage = MutableStateFlow(Language.ENGLISH)
    private val selectedCategory = MutableStateFlow<String?>(null)

    @OptIn(ExperimentalCoroutinesApi::class)
    val uiState: StateFlow<PracticeSetupUiState> =
        selectedLanguage.flatMapLatest { language ->
            combine(
                selectedCategory,
                observeCategoryCounts(language),
                observeWordCount(language, null),
                selectedCategory.flatMapLatest { cat ->
                    observeWordCount(language, cat)
                }
            ) { category, counts, total, selectedCount ->
                PracticeSetupUiState(
                    selectedLanguage = language,
                    selectedCategory = category,
                    categoryCounts = counts,
                    totalCount = total,
                    selectedCount = selectedCount,
                    canStart = selectedCount > 0
                )
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = PracticeSetupUiState()
        )



    fun onLanguageSelected(language: Language) {
        selectedLanguage.value = language
        selectedCategory.value = null // با عوض شدن زبان، دسته ریست
    }

    fun onCategorySelected(category: String?) {
        selectedCategory.value = category
    }

    fun currentConfig(): PracticeConfig {
        val state = uiState.value
        return PracticeConfig(
            language = state.selectedLanguage,
            category = state.selectedCategory,
            mode = PracticeMode.SPELLING
        )
    }






}