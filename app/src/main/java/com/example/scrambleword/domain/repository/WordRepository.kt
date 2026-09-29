package com.example.scrambleword.domain.repository

import com.example.scrambleword.domain.model.CategoryCount
import com.example.scrambleword.domain.model.Language
import com.example.scrambleword.domain.model.Word
import kotlinx.coroutines.flow.Flow

interface WordRepository {

    fun getAllWords(): Flow<List<Word>>

    fun getWordsByLanguage(language: Language): Flow<List<Word>>

    fun getFavoriteWords(): Flow<List<Word>>

    fun searchWords(query: String): Flow<List<Word>>

    suspend fun getWordById(id: Long): Word?

    suspend fun getRandomWords(language: Language, limit: Int = 20): List<Word>


    suspend fun insertWord(word: Word): Long

    suspend fun updateWord(word: Word)

    suspend fun deleteWord(word: Word)


    suspend fun updatePracticeStats(id: Long, correct: Int, wrong: Int)

    suspend fun setFavorite(id: Long, isFavorite: Boolean)

    suspend fun exists(word: String, language: String): Boolean

    fun observeWord(language: Language? = null): Flow<List<Word>>

    fun observeCategoryCounts(language: Language): Flow<List<CategoryCount>>

    fun observeWordCount(language: Language, category: String?): Flow<Int>

    suspend fun getWordsForPractice(
        language: Language,
        category: String?,
        limit: Int
        ):List<Word>


}