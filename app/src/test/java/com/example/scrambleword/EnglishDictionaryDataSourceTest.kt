package com.example.scrambleword

import com.example.scrambleword.data.remote.api.FreeDictionaryApi
import com.example.scrambleword.data.remote.datasorce.EnglishDictionaryDataSource
import com.example.scrambleword.data.remote.dto.FreeDefinitionDto
import com.example.scrambleword.data.remote.dto.FreeDictionaryEntryDto
import com.example.scrambleword.data.remote.dto.FreeMeaningDto
import com.example.scrambleword.data.remote.dto.FreePhoneticDto
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class EnglishDictionaryDataSourceTest {
    private lateinit var api: FreeDictionaryApi
    private lateinit var dataSource: EnglishDictionaryDataSource

    @Before
    fun setup() {
        api = mockk()
        dataSource = EnglishDictionaryDataSource(api)
    }
    @Test
    fun `fetch returns success when api returns entries`() = runTest {
        val dto = FreeDictionaryEntryDto(
            word = "hello",
            phonetics = listOf(
                FreePhoneticDto(
                    text = "/həˈləʊ/",
                    audio = "https://example.com/hello.mp3"
                )
            ),
            meanings = listOf(
                FreeMeaningDto(
                    partOfSpeech = "noun",
                    definitions = listOf(
                        FreeDefinitionDto(definition = "A greeting", example = "Hello!")
                    )
                )
            )
        )
        coEvery { api.getEntries("en", "hello") } returns listOf(dto)

        val result = dataSource.fetch("hello")

        assertTrue(result.isSuccess)
        val info = result.getOrNull()!!
        assertEquals("hello", info.word)
        assertEquals("noun", info.partOfSpeech)
        assertEquals("https://example.com/hello.mp3", info.audioUrl)
    }

    @Test
    fun `fetch returns failure when api returns empty list`() = runTest {
        coEvery { api.getEntries("en", "xyzabc") } returns emptyList()

        val result = dataSource.fetch("xyzabc")

        assertTrue(result.isFailure)
    }

    @Test
    fun `fetch returns failure when api throws`() = runTest {
        coEvery { api.getEntries("en", "hello") } throws RuntimeException("network error")

        val result = dataSource.fetch("hello")

        assertTrue(result.isFailure)
        assertEquals("network error", result.exceptionOrNull()?.message)
    }

    @Test
    fun `fetch trims and lowercases word before calling api`() = runTest {
        coEvery { api.getEntries("en", "hello") } returns listOf(
            FreeDictionaryEntryDto(word = "hello")
        )

        val result = dataSource.fetch("  HeLLo  ")

        assertTrue(result.isSuccess)
        // اگر در DataSource lowercase می‌کنی، coEvery با "hello" درست است
    }


}