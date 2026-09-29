package com.example.scrambleword.data.remote.datasorce

import com.example.scrambleword.data.remote.api.FreeDictionaryApi
import com.example.scrambleword.data.remote.mapper.toDomain
import com.example.scrambleword.domain.model.DictionaryInfo
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class EnglishDictionaryDataSource @Inject constructor(
    private val api: FreeDictionaryApi
) {
    suspend fun fetch(word: String): Result<DictionaryInfo> {

        return try {
            val response = api.getEntries(
                language = "en",
                word = word.trim().lowercase()
            )
            val first = response.firstOrNull()
                ?: return Result.failure(Exception("کلمه پیدا نشد"))

            Result.success(first.toDomain())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}