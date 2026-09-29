package com.example.scrambleword.domain.usecase.word

import com.example.scrambleword.domain.model.Word
import com.example.scrambleword.domain.repository.WordRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchWordUseCase @Inject constructor(
    private val repository: WordRepository
) {

    operator fun invoke(query: String): Flow<List<Word>> {
        return if (query.isBlank()) {
            repository.observeWord(null)
        } else {
            repository.searchWords(query)
        }
    }
}