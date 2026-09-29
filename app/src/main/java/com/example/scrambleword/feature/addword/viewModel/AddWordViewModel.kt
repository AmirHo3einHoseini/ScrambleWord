package com.example.scrambleword.feature.addword.viewModel


import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.savedstate.savedState
import com.example.scrambleword.domain.model.Language
import com.example.scrambleword.domain.model.Word
import com.example.scrambleword.domain.usecase.word.SaveWordUseCase
import com.example.scrambleword.feature.addword.state.AddWordUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class AddWordViewModel @Inject constructor(
    private val saveWordUseCase: SaveWordUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {


    private val _uiState = MutableStateFlow(
        AddWordUiState(
            wordInput = savedStateHandle
                .get<String>("word")
                ?.let { Uri.decode(it) }
                ?.takeIf { it.isNotBlank() && it != "{word}" }
                .orEmpty(),
            selectedLanguage = Language.fromCode(
                savedStateHandle.get<String>("language").orEmpty()
            ) ?: Language.ENGLISH
        )
    )
    val uiState: StateFlow<AddWordUiState> = _uiState.asStateFlow()


    fun onWordChange(value: String) {
        _uiState.update {
            it.copy(
                wordInput = value,
                errorMessage = null
            )
        }
        clearExtraDetails()
    }

    fun meaningChange(value: String) {
        _uiState.update {
            it.copy(meaningInput = value)
        }
    }

    fun onCategorySelected(category: String) {
        _uiState.update { it.copy(selectedCategory = category) }
    }

    fun onHintChange(value: String) {
        _uiState.update {
            it.copy(hintInput = value)
        }
    }

    fun onLanguageChange(value: Language) {
        _uiState.update {
            it.copy(selectedLanguage = value)
        }
    }

    fun searchInDictionary() {
        val word = _uiState.value.wordInput.trim()
        if (word.isBlank()) {
            _uiState.value = _uiState.value.copy(errorMessage = "کلمه را وارد کنید.")
            return
        }
        viewModelScope.launch {

            _uiState.value = _uiState.value.copy(isSaving = true, errorMessage = null)


        }
    }


    fun saveWord() {
        val state = _uiState.value

        if (state.wordInput.isBlank() || state.meaningInput.isBlank()) {
            _uiState.value = state.copy(errorMessage = "کلمه و معنی الزامی هستند")
            return
        }
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isSaving = true,
                    errorMessage = null,
                    showSavedMessage = false
                )
            }
            val word = Word(
                word = state.wordInput.trim(),
                meaning = state.meaningInput.trim(),
                language = state.selectedLanguage,
                category = state.selectedCategory,
                phonetic = state.phonetic,
                audioUrl = state.audioUrl,
                partOfSpeech = state.selectedPartOfSpeech,
                example = state.exampleFromDictionary.ifEmpty { null },
                definitions = state.selectedDefinitions.ifEmpty { null },
                hint = state.hintInput.ifBlank { null },
            )
            val result = saveWordUseCase(word)
            result
                .onSuccess {
                    _uiState.update {
                        it.copy(
                            isSaved = true,
                            isSaving = false,
                            showSavedMessage = true,
                            wordInput = "",
                            meaningInput = "",
                            hintInput = ""
                        )
                    }
                }
                .onFailure { e ->
                    _uiState.update {
                        it.copy(
                            isSaving = false,
                            errorMessage = e.message ?: "خطا در ذخیره کلمه"
                        )
                    }
                }
//            _uiState.value = state.copy(isSaved = true)
        }
    }

    fun resetSavedState() {
        _uiState.update {
            it.copy(
                isSaved = false,
                isSaving = false,
                showSavedMessage = false,
                wordInput = "",
                meaningInput = "",
                hintInput = ""
            )
        }
        clearExtraDetails()
    }


    fun applyDictionaryDetails(phonetic: String?, audioUrl: String?, correctedWord: String?) {
        _uiState.update {
            it.copy(
                wordInput = correctedWord?.takeIf { w -> w.isNotBlank() } ?: it.wordInput,
                phonetic = phonetic,
                audioUrl = audioUrl,
                hasExtraDetails = !phonetic.isNullOrBlank() || !audioUrl.isNullOrBlank()
            )
        }
    }


    fun onDictionaryResult(
        word: String?,
        phonetic: String?,
        audioUrl: String?,
        meaning: String?,
        examples: List<String>
    ) {
        _uiState.update {
            it.copy(
                wordInput = word?.takeIf { w -> w.isNotBlank() } ?: it.wordInput,
                phonetic = phonetic,
                audioUrl = audioUrl,
                meaningInput = meaning?.takeIf { m -> m.isNotBlank() } ?: it.meaningInput,
                exampleFromDictionary = examples
            )
        }
    }


    fun clearExtraDetails() {
        _uiState.update {
            it.copy(
                phonetic = null,
                audioUrl = null,
                hasExtraDetails = false
            )
        }
    }

}