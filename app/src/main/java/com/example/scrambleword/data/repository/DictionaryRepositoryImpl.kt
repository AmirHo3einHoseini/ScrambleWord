package com.example.scrambleword.data.repository

import com.example.scrambleword.data.remote.datasorce.EnglishDictionaryDataSource
import com.example.scrambleword.data.remote.datasorce.WiktDictionaryDataSource
import com.example.scrambleword.domain.model.DictionaryInfo
import com.example.scrambleword.domain.model.Language
import com.example.scrambleword.domain.repository.DictionaryRepository
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class DictionaryRepositoryImpl @Inject constructor(
    private val englishDataSource: EnglishDictionaryDataSource,
    private val wiktDataSource: WiktDictionaryDataSource
) : DictionaryRepository {

    override suspend fun fetchWordInfo(
        word: String,
        language: Language
    ): Result<DictionaryInfo> {
        val cleanWord = word.trim()

        if (cleanWord.isBlank()) {
            return Result.failure(IllegalArgumentException("کلمه نمیتواند خالی باشد"))
        }

        return wiktDataSource.fetch(cleanWord, language)


//        return when (language) {
//            Language.ENGLISH -> englishDataSource.fetch(cleanWord)
//            Language.FRENCH,
//            Language.SPANISH,
//            Language.GERMAN -> wiktDataSource.fetch(cleanWord, language)
//        }
    }
}