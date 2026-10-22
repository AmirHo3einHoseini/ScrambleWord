package com.example.scrambleword.data.remote.exercise

import com.example.scrambleword.domain.model.practice.PracticeDifficulty

interface LessonApi {
    suspend fun getLesson(lessonId: String): LessonDto

    suspend fun buildStepPractice(
        step: LessonStepDto,
        difficulty: PracticeDifficulty = PracticeDifficulty.EASY
    ): FillBlankExerciseDto
}