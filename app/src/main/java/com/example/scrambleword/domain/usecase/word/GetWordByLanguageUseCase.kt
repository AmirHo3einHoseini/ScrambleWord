package com.example.scrambleword.domain.usecase.word

import com.example.scrambleword.domain.model.Language
import com.example.scrambleword.domain.model.Word
import com.example.scrambleword.domain.repository.WordRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetWordByLanguageUseCase @Inject constructor(
    private val repository: WordRepository
) {
    operator fun invoke(language: Language): Flow<List<Word>> {
        return repository.getWordsByLanguage(language)
    }
}