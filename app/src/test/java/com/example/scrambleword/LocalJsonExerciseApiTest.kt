package com.example.scrambleword

import android.content.Context
import android.content.res.AssetManager
import com.example.scrambleword.data.remote.exercise.FillBlankMode
import com.example.scrambleword.data.remote.exercise.LocalJsonExerciseApi
import com.example.scrambleword.domain.model.practice.PracticeDifficulty
import com.google.gson.Gson
import io.mockk.every
import io.mockk.mockk
import java.nio.charset.StandardCharsets
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class LocalJsonExerciseApiTest {

    @Test
    fun getFillBlankOnlyCreatesExercisesForTargetVocabulary() = runBlocking {
        val exercises = createApi(
            confusablesJson = """{"tea":["coffee","water","juice"]}"""
        ).getFillBlank(
            level = "A1",
            limit = 20,
            difficulty = PracticeDifficulty.EASY
        )

        assertTrue(exercises.isNotEmpty())
        assertEquals(setOf("tea"), exercises.map { it.answer }.toSet())
        assertTrue(exercises.all { it.mode == FillBlankMode.CHOICE })
    }

    @Test
    fun choiceOptionsContainCorrectAnswerExactlyOnceWithoutDuplicates() = runBlocking {
        val exercises = createApi(
            confusablesJson = """{"tea":["coffee","water","juice"]}"""
        ).getFillBlank(
            level = "A1",
            limit = 20,
            difficulty = PracticeDifficulty.EASY
        )

        assertTrue(exercises.isNotEmpty())
        exercises.forEach { exercise ->
            val options = requireNotNull(exercise.options)
            assertTrue(
                "Correct answer must be present among options",
                options.any { it.equals(exercise.answer, ignoreCase = true) }
            )
            assertEquals(
                "Correct answer must appear exactly once",
                1,
                options.count { it.equals(exercise.answer, ignoreCase = true) }
            )
            assertEquals(
                "Options must not contain case-insensitive duplicates",
                options.size,
                options.distinctBy { it.trim().lowercase() }.size
            )
        }
    }

    @Test
    fun choiceOptionsHaveFourOptionsWhenEnoughConfusablesExist() = runBlocking {
        val exercises = createApi(
            confusablesJson = """{"tea":["coffee","water","juice"]}"""
        ).getFillBlank(
            level = "A1",
            limit = 20,
            difficulty = PracticeDifficulty.EASY
        )

        assertTrue(exercises.isNotEmpty())
        exercises.forEach { exercise ->
            assertEquals(
                "Expected one correct answer and three distractors",
                4,
                requireNotNull(exercise.options).size
            )
        }
    }

    @Test
    fun choiceOptionsDoNotUseNonTargetVocabularyAsFallbackDistractors() = runBlocking {
        val exercises = createApi(
            confusablesJson = """{"tea":["coffee"]}"""
        ).getFillBlank(
            level = "A1",
            limit = 20,
            difficulty = PracticeDifficulty.EASY
        )

        assertTrue(exercises.isNotEmpty())
        exercises.forEach { exercise ->
            assertFalse(
                "Non-target vocabulary 'please' must not be used as a distractor",
                requireNotNull(exercise.options).any { it.equals("please", ignoreCase = true) }
            )
        }
    }

    private fun createApi(confusablesJson: String): LocalJsonExerciseApi {
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

        every {
            assets.open("packs/restaurant_en_a1_b1_structured_v0_3.json")
        } returns sentencesJson.byteInputStream(StandardCharsets.UTF_8)
        every {
            assets.open("packs/confusables_restaurant_en_v0_1.json")
        } returns confusablesJson.byteInputStream(StandardCharsets.UTF_8)

        return LocalJsonExerciseApi(context, Gson())
    }
}
