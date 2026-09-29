package com.example.scrambleword.domain.usecase.word

import com.example.scrambleword.domain.model.Word
import com.example.scrambleword.domain.repository.WordRepository
import javax.inject.Inject

class SaveWordUseCase @Inject constructor(
    private val repository: WordRepository
) {
    suspend operator fun invoke(word: Word): Result<Long> {
        val exists = repository.exists(
            word = word.word,
            language = word.language.code
        )
        if (exists) {
            return Result.failure(
                IllegalStateException(
                    "این کلمه قبلا ذخیره شده. میتوانی آن را ویرایش کنی"
                )
            )
        }
        val id = repository.insertWord(word)
        return Result.success(id)
    }
}