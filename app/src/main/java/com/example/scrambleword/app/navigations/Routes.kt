package com.example.scrambleword.app.navigations

import android.net.Uri



object Routes {
    const val HOME = "home"
    const val ADD_WORD = "add_word?word={word}&language={language}"
    const val DICTIONARY = "dictionary/{word}/{language}"
    const val WORD_LIST = "word_list"
    const val PRACTICE_SETUP = "practice_setup"
    const val PRACTICE_PLAY = "practice_play?language={language}&category={category}"

    fun addWord(word: String? = null, language: String? = null): String {
        val w = Uri.encode(word.orEmpty())
        val l = language.orEmpty()
        return "add_word?word=$w&language=$l"
    }

    fun dictionary(word: String, language: String): String {
        return "dictionary/${Uri.encode(word)}/$language"
    }

    fun practicePlay(language: String, category: String?): String {
        val cat = Uri.encode(category.orEmpty())
        return "practice_play?language=$language&category=$cat"
    }
}