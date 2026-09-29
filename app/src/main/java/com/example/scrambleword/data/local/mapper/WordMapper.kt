package com.example.scrambleword.data.local.mapper

import com.example.scrambleword.data.local.entity.WordEntity
import com.example.scrambleword.data.seed.PackWordDto
import com.example.scrambleword.domain.model.Language
import com.example.scrambleword.domain.model.Word

fun WordEntity.toDomain() = Word(
    id = id,
    word = word,
    meaning = meaning,
    language = Language.fromCode(language) ?: Language.ENGLISH,
    category = category,
    phonetic = phonetic,
    audioUrl = audioUrl,
    partOfSpeech = partOfSpeech,
    example = example ?: emptyList(),
    synonyms = synonyms,
    definitions = definitions ?: emptyList(),
    hint = hint,
    level = level,
    isFavorite = isFavorite,
    correctCount = correctCount,
    wrongCount = wrongCount,
    lastPracticed = lastPracticed,
    createdAt = createdAt,
    updatedAt = updatedAt
)


fun Word.toEntity() = WordEntity(
    id = id,
    word = word,
    meaning = meaning,
    language = language.code,
    category = category,
    phonetic = phonetic,
    audioUrl = audioUrl,
    partOfSpeech = partOfSpeech,
    example = example?.ifEmpty { null },
    synonyms = synonyms?.ifEmpty { null },
    definitions = definitions,
    hint = hint,
    level = level,
    isFavorite = isFavorite,
    correctCount = correctCount,
    wrongCount = wrongCount,
    lastPracticed = lastPracticed,
    createdAt = createdAt,
    updatedAt = updatedAt
)



fun PackWordDto.toEntityOrNull(): WordEntity? {
    val w = word?.trim().orEmpty()
    val meaning = meaningFa?.trim().orEmpty()
    if (w.isEmpty() || meaning.isEmpty()) return null

    val examples = buildList {
        val en = exampleEn?.trim().orEmpty()
        if (en.isNotEmpty()) {
            val fa = exampleFa?.trim().orEmpty()
            add(if (fa.isNotEmpty()) "$en|$fa" else en)
        }
    }

    return WordEntity(
        word = w,
        meaning = meaning,
        language = language?.trim()?.lowercase().orEmpty().ifEmpty { "en" },
        category = category?.trim().orEmpty().ifEmpty { "عمومی" },
        hint = hint?.trim()?.takeIf { it.isNotEmpty() },
        phonetic = phonetic?.trim()?.takeIf { it.isNotEmpty() },
        audioUrl = audioUrl?.trim()?.takeIf { it.isNotEmpty() },
        example = examples
    )
}