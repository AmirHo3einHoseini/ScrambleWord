package com.example.scrambleword

import com.example.scrambleword.data.remote.dto.FreeDefinitionDto
import com.example.scrambleword.data.remote.dto.FreeDictionaryEntryDto
import com.example.scrambleword.data.remote.dto.FreeMeaningDto
import com.example.scrambleword.data.remote.dto.FreePhoneticDto
import com.example.scrambleword.data.remote.mapper.toDomain
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import junit.framework.TestCase.assertTrue
import org.junit.Test

class FreeDictionaryMapperTest {

    @Test
    fun `maps word phonetic audio partOfSpeech definitions and examples`(){
        val dto = FreeDictionaryEntryDto(
            word = "hello",
            phonetic = null,
            phonetics = listOf(
                FreePhoneticDto(text = "/həˈloʊ/", audio = ""),
                FreePhoneticDto(
                    text = "/həˈləʊ/",
                    audio = "https://api.dictionaryapi.dev/media/pronunciations/en/hello-uk.mp3"
                )
            ),
            meanings = listOf(
                FreeMeaningDto(
                    partOfSpeech = "noun",
                    definitions = listOf(
                        FreeDefinitionDto(
                            definition = "A greeting",
                            example = "She said hello"
                        ),
                        FreeDefinitionDto(
                            definition = "An utterance of hello",
                            example = null
                        )
                    )
                )
            )
        )

        val result = dto.toDomain()
        assertEquals("hello", result.word)
        assertEquals("/həˈloʊ/", result.phonetic) // اولین text غیرخالی
        assertEquals(
            "https://api.dictionaryapi.dev/media/pronunciations/en/hello-uk.mp3",
            result.audioUrl
        )
        assertEquals("noun", result.partOfSpeech)
        assertEquals(listOf("A greeting", "An utterance of hello"), result.definitions)
        assertEquals(listOf("She said hello"), result.examples)
    }

    @Test
    fun `adds https prefix when audio url starts with double slash`() {
        val dto = FreeDictionaryEntryDto(
            word = "test",
            phonetics = listOf(
                FreePhoneticDto(audio = "//ssl.gstatic.com/dictionary/static/sounds/hello.mp3")
            ),
            meanings = emptyList()
        )

        val result = dto.toDomain()

        assertEquals(
            "https://ssl.gstatic.com/dictionary/static/sounds/hello.mp3",
            result.audioUrl
        )
    }

    @Test
    fun `returns empty lists when meanings are missing`() {
        val dto = FreeDictionaryEntryDto(
            word = "x",
            phonetics = emptyList(),
            meanings = emptyList()
        )

        val result = dto.toDomain()

        assertEquals("x", result.word)
        assertNull(result.phonetic)
        assertNull(result.audioUrl)
        assertNull(result.partOfSpeech)
        assertTrue(result.definitions.isEmpty())
        assertTrue(result.examples.isEmpty())
    }


}