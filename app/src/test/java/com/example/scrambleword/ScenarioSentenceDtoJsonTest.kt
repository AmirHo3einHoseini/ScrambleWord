package com.example.scrambleword

import com.example.scrambleword.data.remote.exercise.ScenarioSentenceDto
import com.example.scrambleword.data.remote.exercise.VocabMentionDto
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ScenarioSentenceDtoJsonTest {

    private val gson = Gson()

    @Test
    fun snakeCaseScenarioFieldsAreDeserialized() {
        val json = """
            {
              "id": "SC-0001-s1",
              "scenario": "restaurant",
              "language": "en",
              "level": "A2",
              "step": 1,
              "step_name": "Greeting",
              "speaker": "Waiter",
              "function": "greet",
              "text": "Good afternoon! Here is the menu.",
              "translation_fa": "عصر بخیر! این هم منو.",
              "next_id": "SC-0001-s2",
              "vocab_mentions": [],
              "grammar_mentions": []
            }
        """.trimIndent()

        val dto = gson.fromJson(json, ScenarioSentenceDto::class.java)

        assertEquals("Greeting", dto.stepName)
        assertEquals("SC-0001-s2", dto.nextId)
    }

    @Test
    fun snakeCaseVocabularyMentionFieldsAreDeserialized() {
        val json = """
            [
              {
                "surface": "menu",
                "lemma": "menu",
                "language": "en",
                "pos": "noun",
                "grammatical_role": "core",
                "cefr_estimate": "A1",
                "is_target": true
              },
              {
                "surface": "please",
                "lemma": "please",
                "language": "en",
                "pos": "adverb",
                "grammatical_role": "politeness",
                "cefr_estimate": "A1",
                "is_target": false
              }
            ]
        """.trimIndent()

        val type = object : TypeToken<List<VocabMentionDto>>() {}.type
        val mentions = gson.fromJson<List<VocabMentionDto>>(json, type)

        assertEquals("core", mentions[0].grammaticalRole)
        assertEquals("A1", mentions[0].cefrEstimate)
        assertTrue(mentions[0].isTarget)

        assertEquals("politeness", mentions[1].grammaticalRole)
        assertEquals("A1", mentions[1].cefrEstimate)
        assertFalse(mentions[1].isTarget)
    }
}
