package com.example.scrambleword.data.remote.exercise

import android.R.id.input
import android.content.Context
import com.example.scrambleword.domain.model.practice.PracticeDifficulty
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.InputStreamReader
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalJsonLessonApi @Inject constructor(
    @ApplicationContext private val context: Context,
    private val gson: Gson
) : LessonApi {
    private val confusables: Map<String, List<String>> by lazy { loadConfusable() }


    override suspend fun getLesson(lessonId: String): LessonDto {
        val path = when (lessonId) {
            "restaurant_en_a1_intro" ->
                "packs/lessons/restaurant_en_a1_intro.json"

            else ->
                "packs/lessons/restaurant_en_a1_intro.json"
        }
        context.assets.open(path).use { input ->
            return gson.fromJson(
                InputStreamReader(input, Charsets.UTF_8),
                LessonDto::class.java
            )
        }
    }

    override suspend fun buildStepPractice(
        step: LessonStepDto,
        difficulty: PracticeDifficulty
    ): FillBlankExerciseDto {
        val text = step.learnText
        val surface = step.focusSurface
        val index = text.indexOf(surface, ignoreCase = true)
        require(index >= 0) {
            "focus_surface '$surface' not found in:$text "
        }
        val exact = text.substring(index, index + surface.length)
        val stem = text.substring(0, index) + "_____" +
                text.substring(index + surface.length)

        val mode = when (difficulty) {
            PracticeDifficulty.EASY -> FillBlankMode.CHOICE
            PracticeDifficulty.NORMAL -> FillBlankMode.SCRAMBLE
            PracticeDifficulty.HARD -> FillBlankMode.TYPE
        }
        val options = if (mode == FillBlankMode.CHOICE) {
            buildOptions(exact, step.focusLemma)
        } else null

        val scrambled = if (mode == FillBlankMode.SCRAMBLE) {
            scrambleWord(exact)
        } else null

        return FillBlankExerciseDto(
            exerciseId = "lesson-${step.id}-$exact",
            mode = mode,
            level = "A1",
            scenario = "restaurant",
            sourceSentenceId = step.sentenceId,
            prompt = when (mode) {
                FillBlankMode.CHOICE -> ""
                FillBlankMode.SCRAMBLE -> ""
                FillBlankMode.TYPE -> ""
            },
            stem = stem,
            answer = exact,
            acceptableAnswers = listOf(exact, surface, step.focusLemma).distinct(),
            options = options,
            scrambleHint = scrambled,
            translationFa = step.translationFa,
            hint = if (mode == FillBlankMode.TYPE) step.focusLemma else null,
            targets = FillBlankTargetsDto(
                lemma = step.focusLemma,
                pos = null,
                cefr = "A1"
            )
        )
    }

    private fun buildOptions(answer: String, lemma: String): List<String> {
        val correct = answer.trim()
        val fromMap = confusables[lemma.lowercase()].orEmpty()
            .filter { !it.equals(correct, ignoreCase = true) }
            .distinctBy { it.lowercase() }
            .take(3)
            .toMutableList()

        // اگر confusable کم بود، چند گزینهٔ ساده از همان درس
        val fallbackPool = listOf("menu", "bill", "table", "card", "water", "please")
        if (fromMap.size < 3) {
            fallbackPool
                .filter { !it.equals(correct, ignoreCase = true) }
                .filter { f -> fromMap.none { it.equals(f, ignoreCase = true) } }
                .take(3 - fromMap.size)
                .forEach { fromMap.add(it) }
        }

        return (listOf(correct) + fromMap.take(3)).shuffled()
    }


    private fun scrambleWord(word: String): String {
        if (word.length <= 1) return word
        val chars = word.toCharArray()
        var shuffled: CharArray
        do {
            shuffled = chars.copyOf().also { it.shuffle() }
        } while (String(shuffled).equals(word, ignoreCase = true) && word.length > 2)
        return String(shuffled)
    }


    private fun loadConfusable(): Map<String, List<String>> {
        return try {
            context.assets.open("lexicon/confusables_restaurant_en_v0_1.json").use { input ->
                val type = object : TypeToken<Map<String, List<String>>>() {}.type
                gson.fromJson<Map<String, List<String>>>(
                    InputStreamReader(input, Charsets.UTF_8),
                    type
                ).mapKeys { it.key.lowercase() }
            }
        } catch (e: Exception) {
            emptyMap()
        }
    }

}