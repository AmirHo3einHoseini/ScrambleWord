package com.example.scrambleword.data.remote.mapper

import com.example.scrambleword.data.remote.dto.FreeDictionaryEntryDto
import com.example.scrambleword.domain.model.DictionaryInfo

fun FreeDictionaryEntryDto.toDomain(): DictionaryInfo {

    val audioUrl = phonetics
        .firstOrNull { !it.audio.isNullOrBlank() }
        ?.audio
        ?.let { url ->
            if (url.startsWith("http")) url else "https:$url"
        }

    val phoneticText = phonetic
        ?: phonetics.firstOrNull { !it.text.isNullOrBlank() }?.text

    val partOfSpeech = meanings.firstOrNull()?.partOfSpeech

    val definitions = meanings
        .flatMap { it.definitions }
        .mapNotNull { it.definition }
        .filter { it.isNotBlank() }
        .take(3)

    val example = meanings
        .flatMap { it.definitions }
        .mapNotNull { it.example }
        .filter { it.isNotBlank() }
        .take(2)

    return DictionaryInfo(
        word = word.orEmpty(),
        phonetic = phoneticText,
        audioUrl = audioUrl,
        partOfSpeech = partOfSpeech,
        definitions = definitions,
        examples = example
    )


}