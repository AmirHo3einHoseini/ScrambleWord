package com.example.scrambleword.data.remote.exercise

import android.content.Context
import android.util.Log
import com.example.scrambleword.domain.model.practice.PracticeDifficulty
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.InputStreamReader
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.random.Random


interface ExerciseApi {
    suspend fun getFillBlank(
        level: String,
        limit: Int,
        difficulty: PracticeDifficulty
    ): List<FillBlankExerciseDto>
}




@Singleton
class LocalJsonExerciseApi @Inject constructor(
    @ApplicationContext private val context: Context,
    private val gson: Gson
) : ExerciseApi {

    private val sentences: List<ScenarioSentenceDto> by lazy { loadSentences() }

    private val confusables: Map<String, List<String>> by lazy { loadConfusable() }

    override suspend fun getFillBlank(
        level: String,
        limit: Int,
        difficulty: PracticeDifficulty
    ): List<FillBlankExerciseDto> {
        val mode = when (difficulty) {
            PracticeDifficulty.EASY -> FillBlankMode.CHOICE
            PracticeDifficulty.NORMAL -> FillBlankMode.SCRAMBLE
            PracticeDifficulty.HARD -> FillBlankMode.TYPE
        }
        val pool = sentences
            .filter { it.level.equals(level, ignoreCase = true) }
            .flatMap { s ->
                val mentions = s.vocabMentions.orEmpty()
                mentions
                    .filter { it.isTarget && it.surface.length >= 2 }
                    .mapNotNull { m -> toFillBlank(s, m, mode) }
            }
            .shuffled(Random.Default)
        return pool.take(limit)
    }


    private fun toFillBlank(
        s: ScenarioSentenceDto,
        m: VocabMentionDto,
        mode: FillBlankMode
    ): FillBlankExerciseDto? {
        val text = s.text ?: return null
        val surface = m.surface
        val index = text.indexOf(surface, ignoreCase = true)
        if (index < 0) return null
        val exact = text.substring(index, index + surface.length)
        val stem = text.substring(0, index) + "_______" + text.substring(index + surface.length)
        val lemma = m.lemma.ifBlank { exact }
        val option = if (mode == FillBlankMode.CHOICE) {
            buildOptions(exact, lemma, m.language, m.cefrEstimate)
        } else null

        val scrambled = if (mode == FillBlankMode.SCRAMBLE) {
            scrambleWord(exact)
        } else null
        return FillBlankExerciseDto(
            exerciseId = "fb-${s.id}-${lemma}-${mode.name.lowercase()}",
            mode = mode,
            level = s.level.orEmpty(),
            scenario = s.scenario.orEmpty(),
            sourceSentenceId = s.id.orEmpty(),
            prompt = when (mode) {
                FillBlankMode.CHOICE -> "گزینه درست را انتخاب کن."
                FillBlankMode.SCRAMBLE -> "با کمک حروف درهم،جای خالی را پرکن."
                FillBlankMode.TYPE -> "کلمه مناسب را در جای خالی بنویس."
            },
            stem = stem,
            answer = exact,
            acceptableAnswers = listOf(exact, surface, lemma).distinct(),
            options = option,
            scrambleHint = scrambled,
            translationFa = s.translationFa.orEmpty(),
            hint = if (mode == FillBlankMode.TYPE) lemma else null,
            targets = FillBlankTargetsDto(
                lemma = lemma,
                pos = m.pos,
                cefr = m.cefrEstimate
            )
        )
    }


    private fun buildOptions(answer: String, lemma: String, language: String, cefr: String?): List<String> {
        val correct = answer.trim()
        val fromMap = confusables[lemma.lowercase()].orEmpty()
            .filter { !it.equals(correct, ignoreCase = true) }
            .distinctBy { it.lowercase() }

        val distractors = fromMap.take(3).toMutableList()

        // Fallback uses target vocabulary from the same language and CEFR level when available.
        if (distractors.size < 3) {
            val fallback = sentences
                .flatMap { it.vocabMentions.orEmpty() }
                .filter { mention ->
                    mention.isTarget &&
                            mention.language.equals(language, ignoreCase = true) &&
                            (cefr.isNullOrBlank() || mention.cefrEstimate.equals(cefr, ignoreCase = true))
                }
                .map { it.surface }
                .filter { surface ->
                    surface.length in (correct.length - 2)..(correct.length + 3) &&
                            !surface.equals(correct, ignoreCase = true) &&
                            distractors.none { it.equals(surface, ignoreCase = true) }
                }
                .distinctBy { it.lowercase() }
                .shuffled()
                .take(3 - distractors.size)
            distractors.addAll(fallback)
        }

        return (listOf(correct) + distractors.take(3)).shuffled()
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

    private fun loadSentences(): List<ScenarioSentenceDto> {
        context.assets.open("packs/restaurant_en_a1_b1_structured_v0_3.json").use { input ->
            val type = object : TypeToken<List<ScenarioSentenceDto>>() {}.type
            return gson.fromJson(InputStreamReader(input, Charsets.UTF_8), type)
        }
    }

    private fun loadConfusable(): Map<String, List<String>> {
        return try {
            context.assets.open("packs/confusables_restaurant_en_v0_1.json").use { input ->
                val type = object : TypeToken<Map<String, List<String>>>() {}.type
                gson.fromJson<Map<String, List<String>>>(
                    InputStreamReader(input, Charsets.UTF_8), type
                ).mapKeys { it.key.lowercase() }
            }
        } catch (e: Exception) {
            Log.e("ExerciseApi", "confusables load failed", e)
            emptyMap()
        }
    }

}