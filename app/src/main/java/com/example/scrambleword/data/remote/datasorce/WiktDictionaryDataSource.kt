package com.example.scrambleword.data.remote.datasorce

import com.example.scrambleword.data.remote.api.WiktApi
import com.example.scrambleword.data.remote.mapper.toDomain
import com.example.scrambleword.domain.model.DictionaryInfo
import com.example.scrambleword.domain.model.Language
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WiktDictionaryDataSource @Inject constructor(
    private val api: WiktApi
) {
    suspend fun fetch(word: String, language: Language): Result<DictionaryInfo> {
        return try {
            val trimmed = word.trim()
            var response = api.getWord(
                edition = "en",
                word = trimmed,
                lang = language.code
            )

            if (response.entries.isEmpty()) {
                val capitalized = trimmed.replaceFirstChar {
                    if (it.isLowerCase()) it.titlecase() else it.toString()
                }
                if (capitalized != trimmed) {
                    response = api.getWord(
                        edition = "en",
                        word = capitalized,
                        lang = language.code
                    )
                }
            }

            if (response.entries.isEmpty()) {
                return Result.failure(Exception("کلمه پیدا نشد"))
            }

            Result.success(response.toDomain())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}