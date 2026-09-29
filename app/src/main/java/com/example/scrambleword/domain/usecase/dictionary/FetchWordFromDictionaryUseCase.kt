package com.example.scrambleword.domain.usecase.dictionary

import com.example.scrambleword.domain.model.DictionaryInfo
import com.example.scrambleword.domain.model.Language
import com.example.scrambleword.domain.repository.DictionaryRepository
import javax.inject.Inject

class FetchWordFromDictionaryUseCase @Inject constructor(
    private val repository: DictionaryRepository
) {
    suspend operator fun invoke(
        word: String,
        language: Language
    ): Result<DictionaryInfo> {
        return repository.fetchWordInfo(word, language)
    }
}