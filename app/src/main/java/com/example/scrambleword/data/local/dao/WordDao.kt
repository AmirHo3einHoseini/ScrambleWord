package com.example.scrambleword.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.scrambleword.data.local.entity.CategoryCountRow
import com.example.scrambleword.data.local.entity.WordEntity
import com.example.scrambleword.domain.model.Language
import kotlinx.coroutines.flow.Flow


@Dao
interface WordDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(word: WordEntity): Long

    @Update
    suspend fun updateWord(word: WordEntity)

    @Delete
    suspend fun delete(word: WordEntity)


    @Query("SELECT * FROM words ORDER BY word ASC")
    fun getAllWords(): Flow<List<WordEntity>>

    @Query("SELECT * FROM words WHERE language=:lang ORDER BY word ASC ")
    fun getWordsByLanguage(lang: String): Flow<List<WordEntity>>

    @Query(
        """
         SELECT*FROM words 
         WHERE word LIKE '%' ||:query ||'%'
         OR meaning LIKE '%'||:query||'%'
         ORDER BY language ASC,word COLLATE NOCASE ASC
         """
    )
    fun searchWords(query: String): Flow<List<WordEntity>>


    @Query("SELECT * FROM words WHERE id=:id")
    suspend fun getWordByID(id: Long): WordEntity?


    @Query("SELECT * FROM words WHERE language=:lang ORDER BY RANDOM() LIMIT :limit")
    fun getRandomWords(lang: String, limit: Int): List<WordEntity>

    @Query(
        """
        UPDATE words 
        SET correctCount = correctCount + :correct,
            wrongCount = wrongCount + :wrong,
            lastPracticed = :timestamp,
            updatedAt = :timestamp
        WHERE id = :id
    """
    )
    suspend fun updatePracticeState(
        id: Long,
        correct: Int,
        wrong: Int,
        timestamp: Long = System.currentTimeMillis()
    )


    @Query("SELECT * FROM words WHERE isFavorite =1 ORDER BY updatedAt DESC")
    fun getFavoriteWords(): Flow<List<WordEntity>>


    @Query("UPDATE words set isFavorite =:isFavorite , updatedAt =:timestamp WHERE id =:id")
    suspend fun setFavorite(
        id: Long,
        isFavorite: Boolean,
        timestamp: Long = System.currentTimeMillis()
    )

    @Query(
        """
            SELECT EXISTS(
            SELECT 1 FROM words 
            WHERE LOWER(word) = LOWER(:word)
            AND language=:language
            )
            """
    )
    suspend fun existsByWordAndLanguage(
        word: String,
        language: String
    ): Boolean


    @Query(
        """ 
            SELECT * FROM words
            ORDER BY language ASC,word COLLATE NOCASE ASC
            
                """
    )
    fun observeAllWords(): Flow<List<WordEntity>>


    @Query(
        """
            SELECT * FROM words WHERE language=:language
            ORDER BY word COLLATE NOCASE ASC
        """
    )
    fun observeWordByLanguage(language: String): Flow<List<WordEntity>>


    @Query(
        """
         SELECT category AS category,COUNT(*) AS count
         FROM words 
         WHERE language = :language
         GROUP BY category
         ORDER BY category ASC
               """
    )
    fun observeCategoryCountS(language: String): Flow<List<CategoryCountRow>>

    @Query(
        """
    SELECT COUNT(*) FROM words
    WHERE language = :language
    AND (:category IS NULL OR category= :category)
              """
    )
    fun observeWordCount(language: String, category: String?): Flow<Int>

    @Query("SELECT COUNT(*) FROM words WHERE language = :language")
    fun observeWordCountAll(language: String): Flow<Int>

    @Query("SELECT COUNT(*) FROM words WHERE language = :language AND category = :category")
    fun observeWordCountByCategory(language: String, category: String): Flow<Int>

    @Query(
        """
            SELECT * FROM words 
            WHERE language = :language
            ORDER BY RANDOM()
            LIMIT :limit
        """
    )
    suspend fun getWordsForPracticeAllCategories(
        language: String,
        limit: Int
    ): List<WordEntity>


    @Query(
        """
        SELECT * FROM words 
        WHERE language= :language AND category = :category
        ORDER BY RANDOM()
        LIMIT:limit
    """
    )
    suspend fun getWordsForPracticeByCategory(
        language: String,
        category: String,
        limit: Int
    ): List<WordEntity>


}