package com.example.scrambleword.domain.usecase.word

import com.example.scrambleword.domain.model.Word
import com.example.scrambleword.domain.repository.WordRepository
import javax.inject.Inject

class DeleteWordUseCase @Inject constructor(
    private val repository: WordRepository
) {

    suspend operator fun invoke(word: Word) {
        repository.deleteWord(word)
    }


}