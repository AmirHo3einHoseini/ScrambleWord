package com.example.scrambleword.app.navigations

import android.net.Uri
import com.example.scrambleword.domain.model.practice.PracticeDifficulty


object Routes {
    const val MAIN = "main"
    const val MAIN_HOME = "main/home"
    const val MAIN_PRACTICE = "main/practice"
    const val MAIN_LEARN = "main/learn"
    const val MAIN_WORDS = "main/words"
    const val SETTINGS = "settings"
    const val HOME = "home"
    const val ADD_WORD = "add_word?word={word}&language={language}"
    const val DICTIONARY = "dictionary/{word}/{language}"
    const val WORD_LIST = "word_list"
    const val PRACTICE_SETUP = "practice_setup"
    const val PRACTICE_PLAY =
        "practice_play?language={language}&category={category}&difficulty={difficulty}"
    const val FILL_BLANK = "fill_blank?level={level}&difficulty={difficulty}"
    const val LESSON = "lesson/{lessonId}?difficulty={difficulty}"


    fun fillBlank(level: String, difficulty: String): String {
        return "fill_blank?level=$level&difficulty=$difficulty"
    }

    fun addWord(word: String? = null, language: String? = null): String {
        val w = Uri.encode(word.orEmpty())
        val l = language.orEmpty()
        return "add_word?word=$w&language=$l"
    }

    fun dictionary(word: String, language: String): String {
        return "dictionary/${Uri.encode(word)}/$language"
    }

    fun practicePlay(
        language: String,
        category: String?,
        difficulty: String
    ): String {
        val cat = Uri.encode(category.orEmpty())
        return "practice_play?language=$language&category=$cat&difficulty=$difficulty"
    }

    fun lesson(
        lessonId: String = "lesson-restaurant-en-a1-intro",
        difficulty: PracticeDifficulty = PracticeDifficulty.EASY
    ): String {
        return "lesson/$lessonId?difficulty=${difficulty.name}"
    }
}