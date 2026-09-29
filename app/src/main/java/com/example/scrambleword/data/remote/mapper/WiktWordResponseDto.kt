package com.example.scrambleword.data.remote.mapper

import com.example.scrambleword.data.remote.dto.WiktWordResponseDto
import com.example.scrambleword.domain.model.DictionaryInfo

fun WiktWordResponseDto.toDomain(): DictionaryInfo {
    val entry = entries.maxByOrNull { e ->
        val glossCount = e.senses.sumOf { it.glosses.size }
        val hasAudio = e.sounds.any { !it.mp3_url.isNullOrBlank() }
        val hasIpa = e.sounds.any { !it.ipa.isNullOrBlank() }
        glossCount * 10 + (if (hasAudio) 5 else 0) + (if (hasIpa) 2 else 0) + e.translations.size
    } ?: entries.firstOrNull()

    val phonetic = entry?.sounds
        ?.firstOrNull { !it.ipa.isNullOrBlank() }
        ?.ipa

    val audioUrl = entry?.sounds
        ?.firstOrNull { !it.mp3_url.isNullOrBlank() }
        ?.mp3_url

    val definitions = entry?.senses
        ?.flatMap { it.glosses }
        ?.map { it.trim() }
        ?.filter { it.isNotBlank() }
        ?.distinct()
        ?.take(5)
        .orEmpty()

    val examples = entry?.senses
        ?.flatMap { it.examples }
        ?.mapNotNull { it.text }
        ?.filter { it.isNotBlank() }
        ?.distinct()
        ?.take(3)
        .orEmpty()

    val translationsFa = entry?.translations
        ?.filter { t ->
            val code = t.lang_code ?: t.code
            code.equals("fa", ignoreCase = true)
        }
        ?.mapNotNull { it.word?.trim() }
        ?.filter { it.isNotBlank() }
        ?.distinct()
        ?.take(8)
        .orEmpty()

    return DictionaryInfo(
        word = word.orEmpty(),
        phonetic = phonetic,
        audioUrl = audioUrl,
        partOfSpeech = entry?.pos,
        definitions = definitions,
        examples = examples,
        translationsFa = translationsFa
    )

}