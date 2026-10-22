package com.example.scrambleword.domain.usecase.practice

import com.example.scrambleword.domain.model.practice.PracticeConfig
import com.example.scrambleword.domain.model.Word
import com.example.scrambleword.domain.repository.WordRepository
import javax.inject.Inject

class GetPracticeWordsUseCase @Inject constructor(
    private val repository: WordRepository
) {
    suspend operator fun invoke(config: PracticeConfig): List<Word> {
        return repository.getWordsForPractice(
            language = config.language,
            category = config.category,
            limit=config.wordCount
        )
    }
}