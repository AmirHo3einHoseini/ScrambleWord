package com.example.scrambleword.feature.addword.viewModel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scrambleword.domain.model.Language
import com.example.scrambleword.domain.usecase.dictionary.FetchWordFromDictionaryUseCase
import com.example.scrambleword.feature.addword.state.DictionaryUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class DictionaryViewModel @Inject constructor(
    private val fetchWordFromDictionaryUseCase: FetchWordFromDictionaryUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        DictionaryUiState(
            wordInput = savedStateHandle.get<String>("word").orEmpty(),
            selectedLanguage = Language.fromCode(
                savedStateHandle.get<String>("language").orEmpty()
            ) ?: Language.ENGLISH
        )
    )

    val uiState: StateFlow<DictionaryUiState> = _uiState.asStateFlow()

    init {
        val initial = _uiState.value.wordInput
        if (initial.isNotBlank()) {
            search()
        }
    }


    fun search() {
        val state = _uiState.value
        if (state.wordInput.isBlank()) {
            _uiState.update {
                it.copy(errorMessage = "کلمه را وارد کن.")
            }
            return
        }
        viewModelScope.launch {
            _uiState.update {
                it.copy(isLoading = true, errorMessage = null, result = null)
            }
            val result = fetchWordFromDictionaryUseCase(
                word = state.wordInput.trim(),
                language = state.selectedLanguage
            )

            result
                .onSuccess { info ->
                    _uiState.update {
                        it.copy(isLoading = false, result = info)
                    }
                }
                .onFailure { e ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = e.message ?: "اطلاعاتی پیدا نشد"
                        )
                    }
                }
        }
    }
    fun onTranslationSelected(value: String) {
        _uiState.update {
            it.copy(
                selectedTranslation = if (it.selectedTranslation == value) null else value
            )
        }
    }


    fun onExampleSelected(value: String) {
        _uiState.update {
            it.copy(
                selectedExample = if (it.selectedExample == value) null else value
            )
        }
    }


}