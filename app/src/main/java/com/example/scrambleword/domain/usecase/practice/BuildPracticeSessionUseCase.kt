package com.example.scrambleword.domain.usecase.practice

import com.example.scrambleword.domain.model.PracticeConfig
import com.example.scrambleword.domain.model.PracticeQuestion
import com.example.scrambleword.domain.model.toPracticeQuestion
import javax.inject.Inject

class BuildPracticeSessionUseCase @Inject constructor(
    private val getPracticeWordsUseCase: GetPracticeWordsUseCase
) {

    suspend operator fun invoke(config: PracticeConfig): Result<List<PracticeQuestion>> {
        val words = getPracticeWordsUseCase(config)
        if (words.isEmpty()) {
            return Result.failure(
                IllegalStateException("کلمه ای برای این فیلتر پیدا نشد")
            )
        }
        val questions = words.map { it.toPracticeQuestion() }
        return Result.success(questions)

    }
}