package com.example.scrambleword.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.scrambleword.data.local.converter.Converters

@Entity(tableName = "words")
@TypeConverters(Converters::class)
data class WordEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val word: String,
    val meaning: String,
    val language: String,
    val category: String="عمومی",
    val phonetic: String? = null,
    val audioUrl: String? = null,
    val partOfSpeech: String? = null,
    val example: List<String>? = null,
    val synonyms: List<String>? = null,
    val definitions: List<String>? = null,
    val hint: String? = null,
    val level: Int = 1,
    val isFavorite: Boolean = false,
    val correctCount: Int = 0,
    val wrongCount: Int = 0,
    val lastPracticed: Long? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),

    )