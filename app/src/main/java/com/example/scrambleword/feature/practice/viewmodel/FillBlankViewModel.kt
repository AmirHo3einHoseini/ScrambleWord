package com.example.scrambleword.feature.practice.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scrambleword.data.remote.exercise.ExerciseApi
import com.example.scrambleword.domain.model.practice.PracticeDifficulty
import com.example.scrambleword.feature.practice.state.FillBlankUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class FillBlankViewModel @Inject constructor(
    private val exerciseApi: ExerciseApi,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val level: String =
        savedStateHandle.get<String>("level") ?: "A1"

    private val difficulty: PracticeDifficulty = try {
        PracticeDifficulty.valueOf(
            savedStateHandle.get<String>("difficulty")
                ?: PracticeDifficulty.EASY.name
        )
    } catch (_: Exception) {
        PracticeDifficulty.EASY
    }
    private val _uiState =
        MutableStateFlow(FillBlankUiState(level = level, difficulty = difficulty))
    val uiState: StateFlow<FillBlankUiState> = _uiState.asStateFlow()

    init {
        load()
    }

    fun load() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                val list = exerciseApi.getFillBlank(
                    level = "A1",
                    limit = 5,
                    difficulty = difficulty
                )
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        exercises = list,
                        index = 0,
                        userInput = "",
                        checked = false,
                        isCorrect = null,
                        sessionFinished = list.isEmpty(),
                        correctCount = 0
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(isLoading = false, error = e.message ?: "خطا در بارگذاری")
                }
            }
        }
    }

    fun onInputChange(value: String) {
        if (_uiState.value.checked) return
        _uiState.update { it.copy(userInput = value) }
    }

    fun onCheck() {
        val state = _uiState.value
        val ex = state.current ?: return
        if (state.checked) return
        val answer = state.userInput.trim()
        val ok = ex.acceptableAnswers.any { it.equals(answer, ignoreCase = true) }
        _uiState.update {
            it.copy(
                checked = true,
                isCorrect = ok,
                correctCount = if (ok) it.correctCount + 1 else it.correctCount
            )
        }
    }

    fun onNext() {
        val state = _uiState.value
        val next = state.index + 1
        if (next >= state.exercises.size) {
            _uiState.update { it.copy(sessionFinished = true) }
        } else {
            _uiState.update {
                it.copy(
                    index = next,
                    userInput = "",
                    checked = false,
                    isCorrect = null
                )
            }
        }
    }

    fun onOptionSelected(option: String) {
        if (_uiState.value.checked) return
        _uiState.update { it.copy(userInput = option) }
    }

}