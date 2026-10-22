package com.example.scrambleword.feature.practice.viewmodel

import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scrambleword.domain.model.Language
import com.example.scrambleword.domain.model.practice.CubeLetter
import com.example.scrambleword.domain.model.practice.PracticeConfig
import com.example.scrambleword.domain.model.practice.PracticeDifficulty
import com.example.scrambleword.domain.model.practice.PracticeMode
import com.example.scrambleword.domain.model.practice.PracticeQuestion
import com.example.scrambleword.domain.model.practice.PracticeSlot
import com.example.scrambleword.domain.model.practice.SlotsStroke
import com.example.scrambleword.domain.model.practice.baseScore
import com.example.scrambleword.domain.model.practice.stroke
import com.example.scrambleword.domain.model.practice.toPracticeQuestion
import com.example.scrambleword.domain.model.practice.undoPenalty
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

    private val difficulty = try {
        PracticeDifficulty.valueOf(
            savedStateHandle.get<String>("difficulty") ?: PracticeDifficulty.NORMAL.name
        )
    } catch (e: Exception) {
        PracticeDifficulty.NORMAL
    }

    private val config = PracticeConfig(
        language = language,
        category = categoryArg,
        mode = PracticeMode.SPELLING,
        wordCount = 10,
        difficulty = difficulty
    )
    private var sessionScore = 0
    private var perfectCount = 0
    private var perfectStreak = 0
    private var wordMistakeCount = 0
    private var wordUsedHint = false
    private var wordUndoCount = 0
    private var wordPenalty = 0
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
        wordMistakeCount = 0
        wordUsedHint = false
        wordUndoCount = 0
        wordPenalty = 0
        val showHintAlways = difficulty == PracticeDifficulty.EASY
        val hintBtn = difficulty == PracticeDifficulty.NORMAL && !word.hint.isNullOrBlank()
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
                isSessionFinished = false,
                difficulty = difficulty,
                sessionScore = sessionScore,
                lastWordPoints = null,
                perfectCount = perfectCount,
                perfectStreak = perfectStreak,
                showHint = showHintAlways && !word.hint.isNullOrBlank(),
                hintButtonVisible = hintBtn,
                canUseHint = hintBtn,
                wordMistakeCount = 0,
                wordUsedHint = false
            )
        }
    }

    fun onBankCubeClick(cubeId: Int) {
        val state = _uiState.value
        if (state.isCompleted || state.isLoading) return

        val cube = state.cubes.find { it.id == cubeId } ?: return
        if (!cube.isInBank) return

        val emptySlotIndex = state.slots.indexOfFirst { it.cubeId == null }
        if (emptySlotIndex < 0) return

        val expected = state.slots[emptySlotIndex].expected
        if (difficulty == PracticeDifficulty.HARD) {
            if (!cube.char.equals(expected, ignoreCase = true)) {
                wordMistakeCount++
                wordPenalty += 2
                _uiState.update {
                    it.copy(wordMistakeCount = wordMistakeCount)
                }
                return
            }
        }
        val newCubes = state.cubes.map {
            if (it.id == cubeId) it.copy(isInBank = false) else it
        }
        val newSlots = state.slots.mapIndexed { index, slot ->
            if (index == emptySlotIndex) slot.copy(cubeId = cubeId) else slot
        }

        if (difficulty != PracticeDifficulty.HARD) {
            val placed = cube.char
            if (!placed.equals(expected, ignoreCase = true)) {
                wordMistakeCount++
            }
        }
        val completed = newSlots.all { slot ->
            slot.cubeId != null && slot.stroke(newCubes) == SlotsStroke.Correct
        }

        if (completed) {
            onWordComCompleted(newCubes, newSlots)
        } else {

            _uiState.update {
                it.copy(
                    cubes = newCubes,
                    slots = newSlots,
                    isCompleted = completed
                )
            }
        }
    }

    private fun onWordComCompleted(
        newCubes: List<CubeLetter>,
        newSlots: List<PracticeSlot>
    ) {
        val isPerfect = wordMistakeCount == 0 && !wordUsedHint && wordUndoCount == 0
        var points = 1

        if (isPerfect) {
            points += 1
            if (difficulty == PracticeDifficulty.HARD){
                points +=1
            }
            perfectStreak +=1
            perfectCount +=1
        }else{
            perfectStreak=0
        }

        sessionScore += points
        _uiState.update {
            it.copy(
                cubes = newCubes,
                slots = newSlots,
                isCompleted = true,
                sessionScore = sessionScore,
                lastWordPoints = points,
                perfectCount = perfectCount,
                perfectStreak = perfectStreak,
                wordMistakeCount = wordMistakeCount,
                wordUsedHint = wordUsedHint

            )
        }
    }

    fun onSlotClick(slotIndex: Int) {
        val state = _uiState.value
        if (state.isLoading || state.isCompleted) return

        val slot = state.slots.getOrNull(slotIndex) ?: return
        val cubeId = slot.cubeId ?: return

        wordUndoCount++
        wordPenalty += difficulty.undoPenalty()

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
            _uiState.update { it.copy(isSessionFinished = true, isCompleted = false) }
        } else {
            val word = questions[nextIndex].word
            val rebuilt = word.toPracticeQuestion()
            questions = questions.toMutableList().also {
                it[nextIndex] = rebuilt
            }
            showQuestion(nextIndex)
        }
    }

    fun onHintClick() {
        if (difficulty != PracticeDifficulty.NORMAL) return
        if (wordUsedHint) return
        val hint = _uiState.value.hint
        if (hint.isNullOrBlank()) return
        wordUsedHint = true
        _uiState.update {
            it.copy(
                showHint = true,
                canUseHint = false,
                wordUsedHint = true
            )
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