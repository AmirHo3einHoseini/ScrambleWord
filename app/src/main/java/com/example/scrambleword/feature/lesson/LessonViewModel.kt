package com.example.scrambleword.feature.lesson

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scrambleword.data.remote.exercise.LessonApi
import com.example.scrambleword.domain.model.practice.PracticeDifficulty
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LessonViewModel @Inject constructor(
    private val lessonApi: LessonApi, savedStateHandle: SavedStateHandle
) : ViewModel() {


    private val lessonId: String = savedStateHandle.get<String>("lessonId")
        ?: "restaurant_en_a1_intro"

    private val difficulty: PracticeDifficulty = try {
        PracticeDifficulty.valueOf(
            savedStateHandle.get<String>("difficulty")
                ?: PracticeDifficulty.EASY.name
        )
    } catch (_: Exception) {
        PracticeDifficulty.EASY
    }


    private val _uiState = MutableStateFlow(
        LessonUiState(
            lessonId = lessonId, difficulty = difficulty
        )
    )

    val uiState: StateFlow<LessonUiState> = _uiState.asStateFlow()

    init {
        load()
    }

     fun load() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(phase = LessonPhase.LOADING, error = null)
            }
            try {
                val lesson = lessonApi.getLesson(lessonId)
                val steps = lesson.steps.sortedBy { it.order }
                if (steps.isEmpty()) {
                    _uiState.update {
                        it.copy(
                            phase = LessonPhase.ERROR,
                            error = "این درس قدمی ندارد"
                        )
                    }
                    return@launch
                }
                _uiState.update {
                    it.copy(
                        phase = LessonPhase.LEARNING,
                        titleFa = lesson.titleFa,
                        level = lesson.level,
                        steps = steps,
                        stepIndex = 0,
                        exercise = null,
                        userInput = "",
                        checked = false,
                        isCorrect = null,
                        correctCount = 0
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        phase = LessonPhase.ERROR,
                        error = e.message ?: "حطا در بارگذاری درس"
                    )
                }
            }
        }
    }


    fun onContinueFromLearn() {
        val state = _uiState.value
        val step = state.currentStep ?: return
        if (state.phase != LessonPhase.LEARNING) return
        viewModelScope.launch {
            try {
                val exercise = lessonApi.buildStepPractice(step, state.difficulty)
                _uiState.update {
                    it.copy(
                        phase = LessonPhase.PRACTICING,
                        exercise = exercise,
                        userInput = "",
                        checked = false,
                        isCorrect = null
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        phase = LessonPhase.ERROR,
                        error = e.message ?: "حطا در ساخت تمرین"
                    )
                }
            }
        }
    }

    fun onInputChange(value: String) {
        if (_uiState.value.checked) return
        _uiState.update { it.copy(userInput = value) }
    }

    fun onOptionSelected(option: String) {
        if (_uiState.value.checked) return
        _uiState.update { it.copy(userInput = option) }
    }

    fun onCheck() {
        val state = _uiState.value
        val exercise = state.exercise ?: return
        if (state.phase != LessonPhase.PRACTICING || state.checked) return

        val ok = exercise.acceptableAnswers.any {
            it.equals(state.userInput.trim(), ignoreCase = true)
        }
        _uiState.update {
            it.copy(
                checked = true,
                isCorrect = ok,
                correctCount = if (ok) it.correctCount + 1 else it.correctCount
            )
        }
    }

    fun onNextStep() {
        val state = _uiState.value
        if (state.phase != LessonPhase.PRACTICING || !state.checked) return

        if (state.isLastStep) {
            _uiState.update { it.copy(phase = LessonPhase.LESSON_DONE) }
            return
        }

        _uiState.update {
            it.copy(
                stepIndex = it.stepIndex + 1,
                phase = LessonPhase.LEARNING,
                exercise = null,
                userInput = "",
                checked = false,
                isCorrect = null
            )
        }
    }

    fun onRetryLesson() {
        load()
    }
}