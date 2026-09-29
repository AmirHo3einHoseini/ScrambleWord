package com.example.scrambleword.feature.practice.viewmodel

import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scrambleword.domain.model.Language
import com.example.scrambleword.domain.model.PracticeConfig
import com.example.scrambleword.domain.model.PracticeMode
import com.example.scrambleword.domain.model.PracticeQuestion
import com.example.scrambleword.domain.model.SlotsStroke
import com.example.scrambleword.domain.model.stroke
import com.example.scrambleword.domain.model.toPracticeQuestion
import com.example.scrambleword.domain.usecase.practice.BuildPracticeSessionUseCase
import com.example.scrambleword.feature.practice.state.PracticePlayUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class PracticePlayViewModel @Inject constructor(
    private val buildPracticeSessionUseCase: BuildPracticeSessionUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val language = Language.fromCode(
        savedStateHandle.get<String>("language").orEmpty()
    ) ?: Language.ENGLISH

    private val categoryArg = savedStateHandle
        .get<String>("category")
        ?.let { Uri.decode(it) }
        ?.takeIf { it.isNotBlank() }

    private val config = PracticeConfig(
        language = language,
        category = categoryArg,
        mode = PracticeMode.SPELLING
    )
    private var questions: List<PracticeQuestion> = emptyList()

    private val _uiState = MutableStateFlow(PracticePlayUiState())
    val uiState: StateFlow<PracticePlayUiState> = _uiState.asStateFlow()

    init {
        loadSession()
    }

    private fun loadSession() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(isLoading = true, errorMessage = null)
            }

            buildPracticeSessionUseCase(config)
                .onSuccess { list ->
                    questions = list
                    if (list.isEmpty()) {
                        _uiState.update {

                            it.copy(
                                isLoading = false,
                                errorMessage = "کلمه ای برای تمرین پیدا نشد"

                            )
                        }
                    } else {
                        showQuestion(0)
                    }
                }
                .onFailure { e ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = e.message ?: "خطا در بارگذاری تمرین"
                        )
                    }
                }
        }
    }


    private fun showQuestion(index: Int) {
        val question = questions[index]
        val word = question.word
        _uiState.update {
            it.copy(
                isLoading = false,
                errorMessage = null,
                currentIndex = index,
                totalCount = questions.size,
                targetWord = word.meaning,
                hint = word.hint,
                phonetic = word.phonetic,
                audioUrl = word.audioUrl,
                cubes = question.cubes,
                slots = question.slots,
                isCompleted = false,
                isSessionFinished = false
            )
        }
    }

    fun onBankCubeClick(cubeId: Int){
        val state = _uiState.value
        if (state.isCompleted || state.isLoading) return

        val cube = state.cubes.find { it.id == cubeId } ?: return
        if (!cube.isInBank) return

        val emptySlotIndex = state.slots.indexOfFirst { it.cubeId == null }
        if (emptySlotIndex < 0) return

        val newCubes = state.cubes.map {
            if (it.id == cubeId) it.copy(isInBank = false) else it
        }
        val newSlots = state.slots.mapIndexed { index, slot ->
            if (index == emptySlotIndex) slot.copy(cubeId = cubeId) else slot
        }

        val completed = newSlots.all { slot ->
            slot.cubeId != null && slot.stroke(newCubes) == SlotsStroke.Correct
        }

        _uiState.update {
            it.copy(
                cubes = newCubes,
                slots = newSlots,
                isCompleted = completed
            )
        }
    }

    fun onSlotClick(slotIndex: Int) {
        val state = _uiState.value
        if (state.isLoading) return

        val slot = state.slots.getOrNull(slotIndex) ?: return
        val cubeId = slot.cubeId ?: return

        val newSlots = state.slots.mapIndexed { index, s ->
            if (index == slotIndex) s.copy(cubeId = null) else s
        }
        val newCubes = state.cubes.map {
            if (it.id == cubeId) it.copy(isInBank = true) else it
        }

        _uiState.update {
            it.copy(
                cubes = newCubes,
                slots = newSlots,
                isCompleted = false
            )
        }
    }

    fun onNextWord() {
        val nextIndex = _uiState.value.currentIndex + 1
        if (nextIndex >= questions.size) {
            _uiState.update { it.copy(isSessionFinished = true) }
        } else {
            val word = questions[nextIndex].word
            val rebuilt = word.toPracticeQuestion()
            questions = questions.toMutableList().also {
                it[nextIndex] = rebuilt
            }
            showQuestion(nextIndex)
        }
    }

    fun onRetryCurrent() {
        val index = _uiState.value.currentIndex
        if (index !in questions.indices) return
        val rebuilt = questions[index].word.toPracticeQuestion()
        questions = questions.toMutableList().also {
            it[index] = rebuilt
        }
        showQuestion(index)
    }

}