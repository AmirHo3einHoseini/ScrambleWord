package com.example.scrambleword.data.repository

import com.example.scrambleword.data.local.dao.WordDao
import com.example.scrambleword.data.local.mapper.toDomain
import com.example.scrambleword.data.local.mapper.toEntity
import com.example.scrambleword.domain.model.CategoryCount
import com.example.scrambleword.domain.model.Word
import com.example.scrambleword.domain.model.Language
import com.example.scrambleword.domain.repository.WordRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class WordRepositoryImpl @Inject constructor(
    private val wordDao: WordDao
) : WordRepository {
    override fun getAllWords(): Flow<List<Word>> {
        return wordDao.getAllWords().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getWordsByLanguage(language: Language): Flow<List<Word>> {
        return wordDao.getWordsByLanguage(language.code).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getFavoriteWords(): Flow<List<Word>> {
        return wordDao.getFavoriteWords().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun searchWords(query: String): Flow<List<Word>> {
        return wordDao.searchWords(query.trim()).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun getWordById(id: Long): Word? {
        return wordDao.getWordByID(id)?.toDomain()
    }

    override suspend fun getRandomWords(
        language: Language,
        limit: Int
    ): List<Word> {
        return wordDao.getRandomWords(language.code, limit).map { it.toDomain() }
    }

    override suspend fun insertWord(word: Word): Long {
        return wordDao.insert(word.toEntity())
    }

    override suspend fun updateWord(word: Word) {
        wordDao.updateWord(word.toEntity())
    }

    override suspend fun deleteWord(word: Word) {
        wordDao.delete(word.toEntity())
    }

    override suspend fun updatePracticeStats(
        id: Long,
        correct: Int,
        wrong: Int
    ) {
        wordDao.updatePracticeState(id, correct, wrong)
    }

    override suspend fun setFavorite(id: Long, isFavorite: Boolean) {
        wordDao.setFavorite(id, isFavorite)
    }

    override suspend fun exists(word: String, language: String): Boolean {
        return wordDao.existsByWordAndLanguage(
            word = word.trim(),
            language = language
        )

    }

    override fun observeWord(language: Language?): Flow<List<Word>> {
        val source = if (language == null) {
            wordDao.observeAllWords()
        } else {
            wordDao.observeWordByLanguage(language.code)
        }
        return source.map { list -> list.map { it.toDomain() } }
    }

    override fun observeCategoryCounts(language: Language): Flow<List<CategoryCount>> {
        return wordDao.observeCategoryCountS(language.code).map { rows ->
            rows.map { CategoryCount(it.category, it.count) }
        }
    }

    override fun observeWordCount(
        language: Language,
        category: String?
    ): Flow<Int> {
        return if (category == null) {
            wordDao.observeWordCountAll(language.code)
        } else {
            wordDao.observeWordCountByCategory(language.code, category)
        }
    }


    override suspend fun getWordsForPractice(
        language: Language,
        category: String?,
        limit: Int
    ): List<Word> {
        val entities = if (category == null) {
            wordDao.getWordsForPracticeAllCategories(language.code, limit)
        } else {
            wordDao.getWordsForPracticeByCategory(language.code, category, limit)
        }
        return entities.map { it.toDomain() }
    }
}