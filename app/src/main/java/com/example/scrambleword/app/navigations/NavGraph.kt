package com.example.scrambleword.app.navigations

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.scrambleword.domain.model.practice.PracticeDifficulty
import com.example.scrambleword.feature.addword.ui.AddWordScreen
import com.example.scrambleword.feature.addword.ui.DictionaryScreen
import com.example.scrambleword.feature.addword.viewModel.AddWordViewModel
import com.example.scrambleword.feature.home.ui.HomeScreen
import com.example.scrambleword.feature.lesson.LessonScreen
import com.example.scrambleword.feature.practice.ui.FillBlankScreen
import com.example.scrambleword.feature.practice.ui.PracticePlayScreen
import com.example.scrambleword.feature.practice.ui.PracticeSetupScreen
import com.example.scrambleword.feature.wordlist.ui.WordListScreen


@Composable
fun AppNavGraph(
    navController: NavHostController
) {

    NavHost(
        navController = navController,
        startDestination = Routes.HOME
    ) {
        composable(Routes.HOME) {
            HomeScreen(
                onAddWordClick = {
                    navController.navigate(Routes.ADD_WORD)
                },
                onWordListClick = { navController.navigate(Routes.WORD_LIST) },
                onPracticeClick = { navController.navigate(Routes.PRACTICE_SETUP) }


            )
        }

        composable(
            route = Routes.ADD_WORD,
            arguments = listOf(
                navArgument("word") {
                    type = NavType.StringType
                    defaultValue = ""
                },
                navArgument("language") {
                    type = NavType.StringType
                    defaultValue = ""
                }
            )
        ) { entry ->
            val viewModel: AddWordViewModel = hiltViewModel()

            val dictWord by entry.savedStateHandle
                .getStateFlow<String?>("dict_word", null)
                .collectAsStateWithLifecycle()

            val dicPhonetic by entry.savedStateHandle
                .getStateFlow<String?>("dict_phonetic", null)
                .collectAsStateWithLifecycle()

            val dicAudio by entry.savedStateHandle
                .getStateFlow<String?>("dict_audio", null)
                .collectAsStateWithLifecycle()

            val dictMeaning by entry.savedStateHandle
                .getStateFlow<String?>("dict_meaning", null)
                .collectAsStateWithLifecycle()

            val dictExamples by entry.savedStateHandle
                .getStateFlow<String?>("dict_examples", null)
                .collectAsStateWithLifecycle()

            LaunchedEffect(dictWord, dicPhonetic, dicAudio, dictMeaning, dictExamples) {

                if (dictWord != null || dicPhonetic != null || dicAudio != null
                    || dictMeaning != null || dictExamples != null
                ) {
                    viewModel.onDictionaryResult(
                        word = dictWord,
                        phonetic = dicPhonetic,
                        audioUrl = dicAudio,
                        meaning = dictMeaning,
                        examples = dictExamples
                            ?.split("\n")
                            ?.filter { it.isNotBlank() }
                            .orEmpty()
                    )
                    entry.savedStateHandle.remove<String>("dict_word")
                    entry.savedStateHandle.remove<String>("dict_phonetic")
                    entry.savedStateHandle.remove<String>("dict_audio")
                    entry.savedStateHandle.remove<String>("dict_meaning")
                    entry.savedStateHandle.remove<String>("dict_examples")
                }
            }

            AddWordScreen(
                onBack = {
                    navController.popBackStack()
                },
                onOpenDetails = { word, language ->
                    navController.navigate(Routes.dictionary(word, language))
                },
                onPlayAudio = { url ->
                    // فعلاً ساده؛ بعداً Player مرکزی
                    // می‌توانی از یک AudioHelper استفاده کنی
                },
                viewModel = viewModel
            )
        }

        composable(
            route = Routes.DICTIONARY,
            arguments = listOf(
                navArgument("word") { type = NavType.StringType },
                navArgument("language") { type = NavType.StringType }
            )
        ) {
            DictionaryScreen(
                onBack = { navController.popBackStack() },
                onUseInForm = { info, selectedTranslation ->

                    val previous = navController.previousBackStackEntry
                        ?.savedStateHandle

                    previous?.set("dict_word", info.word)
                    previous?.set("dict_phonetic", info.phonetic)
                    previous?.set("dict_audio", info.audioUrl)
                    previous?.set("dict_meaning", selectedTranslation)
                    previous?.set(
                        "dict_examples",
                        info.examples.joinToString("\n")
                    )


                    navController.popBackStack()


                },
                onPlayAudio = { url ->
                    // همان player
                }
            )
        }

        composable(Routes.WORD_LIST) {
            WordListScreen(
                onBack = { navController.popBackStack() },
                onAddWord = { prefillWord, prefillLanguage ->
                    navController.navigate(
                        Routes.addWord(
                            word = prefillWord,
                            language = prefillLanguage?.code
                        )
                    )
                },
                onWordClick = {}
            )
        }

        composable(
            Routes.PRACTICE_SETUP
        ) {
            PracticeSetupScreen(
                onBack = { navController.popBackStack() },
                onStart = { config ->
                    navController.navigate(
                        Routes.practicePlay(
                            language = config.language.code,
                            category = config.category,
                            difficulty = config.difficulty.name
                        )
                    )
                },
                onFill = { config ->
                    navController.navigate(
                        Routes.fillBlank(
                            level = "A1",
                            difficulty = config.difficulty.name
                        )
                    )
                },
                lesson = {
                    navController.navigate(
                        Routes.lesson(
                            lessonId = "restaurant_en_a1_intro",
                            difficulty = PracticeDifficulty.EASY
                        )
                    )
                }
            )
        }

        composable(
            route = Routes.PRACTICE_PLAY,
            arguments = listOf(
                navArgument("language") { type = NavType.StringType },
                navArgument("category") {
                    type = NavType.StringType
                    defaultValue = ""
                },
                navArgument("difficulty") {
                    type = NavType.StringType
                    defaultValue = PracticeDifficulty.NORMAL.name
                }
            )
        ) {
            PracticePlayScreen(
                onBack = { navController.popBackStack() }
            )
        }
        composable(
            Routes.FILL_BLANK,
            arguments = listOf(
                navArgument("level") {
                    type = NavType.StringType
                    defaultValue = "A1"
                },
                navArgument("difficulty") {
                    type = NavType.StringType
                    defaultValue = PracticeDifficulty.EASY.name
                }
            )
        ) {
            FillBlankScreen(onBack = { navController.popBackStack() })
        }

        composable(
            route = Routes.LESSON,
            arguments = listOf(
                navArgument("lessonId") {
                    type = NavType.StringType
                    defaultValue = "restaurant_en_a1_intro"
                },
                navArgument("difficulty") {
                    type = NavType.StringType
                    defaultValue = PracticeDifficulty.EASY.name
                }
            )
        ) {
            LessonScreen(
                onBack = { navController.popBackStack() }
            )
        }

    }


}