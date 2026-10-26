package com.example.scrambleword

import android.content.Context
import android.content.res.AssetManager
import com.example.scrambleword.data.remote.exercise.FillBlankMode
import com.example.scrambleword.data.remote.exercise.LocalJsonExerciseApi
import com.example.scrambleword.domain.model.practice.PracticeDifficulty
import com.google.gson.Gson
import io.mockk.every
import io.mockk.mockk
import java.io.ByteArrayInputStream
import java.nio.charset.StandardCharsets
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class LocalJsonExerciseApiTest {

    @Test
    fun getFillBlankOnlyCreatesExercisesForTargetVocabulary() = runBlocking {
        val context = mockk<Context>()
        val assets = mockk<AssetManager>()
        every { context.assets } returns assets

        val sentencesJson = """
            [
              {
                "id": "test-001",
                "scenario": "restaurant",
                "language": "en",
                "level": "A1",
                "step": 1,
                "step_name": "Ordering",
                "speaker": "Customer",
                "function": "order_food",
                "text": "I would like tea, please.",
                "translation_fa": "من چای می‌خواهم، لطفاً.",
                "vocab_mentions": [
                  {
                    "surface": "tea",
                    "lemma": "tea",
                    "language": "en",
                    "pos": "noun",
                    "cefr_estimate": "A1",
                    "is_target": true
                  },
                  {
                    "surface": "please",
                    "lemma": "please",
                    "language": "en",
                    "pos": "adverb",
                    "cefr_estimate": "A1",
                    "is_target": false
                  }
                ],
                "grammar_mentions": []
              }
            ]
        """.trimIndent()

        val confusablesJson = """{"tea":["coffee","water","juice"]}"""

        every {
            assets.open("packs/restaurant_en_a1_b1_structured_v0_3.json")
        } returns sentencesJson.byteInputStream(StandardCharsets.UTF_8)
        every {
            assets.open("packs/confusables_restaurant_en_v0_1.json")
        } returns confusablesJson.byteInputStream(StandardCharsets.UTF_8)

        val api = LocalJsonExerciseApi(context, Gson())

        val exercises = api.getFillBlank(
            level = "A1",
            limit = 20,
            difficulty = PracticeDifficulty.EASY
        )

        assertTrue(exercises.isNotEmpty())
        assertEquals(setOf("tea"), exercises.map { it.answer }.toSet())
        assertTrue(exercises.all { it.mode == FillBlankMode.CHOICE })
    }
}
