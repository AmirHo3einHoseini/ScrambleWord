package com.example.scrambleword.domain.usecase.practice

import com.example.scrambleword.domain.model.Language
import com.example.scrambleword.domain.repository.WordRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveWordCountUseCase @Inject constructor(
    private val repository: WordRepository
) {
    operator fun invoke(language: Language, category: String?): Flow<Int> {
        return repository.observeWordCount(language, category)
    }
}
