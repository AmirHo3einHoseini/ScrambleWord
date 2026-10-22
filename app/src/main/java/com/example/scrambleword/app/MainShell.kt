package com.example.scrambleword.app

import android.R.attr.type
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Style
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.Navigation
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.scrambleword.app.navigations.MainTab
import com.example.scrambleword.app.navigations.Routes
import com.example.scrambleword.domain.model.practice.PracticeDifficulty
import com.example.scrambleword.feature.home.ui.HomeScreen
import com.example.scrambleword.feature.lesson.LearnHubScreen
import com.example.scrambleword.feature.lesson.LessonScreen
import com.example.scrambleword.feature.practice.ui.FillBlankScreen
import com.example.scrambleword.feature.practice.ui.PracticeHubScreen
import com.example.scrambleword.feature.practice.ui.PracticePlayScreen
import com.example.scrambleword.feature.practice.ui.PracticeSetupScreen
import com.example.scrambleword.feature.wordlist.ui.WordListScreen


fun shouldShowBottomBar(route: String?): Boolean {
    if (route == null) return false
    return route.startsWith("main")
}

@Composable
fun MainShell(
    navController: NavHostController = rememberNavController(),
    startDestination: String = Routes.MAIN_HOME
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val showBottomBar = shouldShowBottomBar(currentRoute)
    val selectedTab = MainTab.fromRoute(currentRoute)



    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar {
                    MainTab.entries.forEach { tab ->
                        NavigationBarItem(
                            selected = selectedTab == tab,
                            onClick = {
                                if (selectedTab != tab) {
                                    navController.navigate(tab.route) {
                                        popUpTo(Routes.MAIN_HOME) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            },
                            icon = {
                                Icon(
                                    imageVector = when (tab) {

                                        MainTab.HOME -> Icons.Default.Home
                                        MainTab.PRACTICE -> Icons.Default.FitnessCenter
                                        MainTab.LEARN -> Icons.AutoMirrored.Filled.MenuBook
                                        MainTab.WORDS -> Icons.Default.Style
                                    },
                                    contentDescription = tab.labelFa
                                )
                            },
                            label = { Text(tab.labelFa) }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Routes.MAIN_HOME) {
                HomeScreen(
                    onOpenSetting = {
                        navController.navigate(Routes.SETTINGS)
                    },
                    onStartLesson = {
                        navController.navigate(Routes.lesson())
                    },
                    onOpenPractice = {
                        navController.navigate(Routes.MAIN_PRACTICE) {
                            popUpTo(Routes.MAIN_HOME) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
            composable(Routes.MAIN_PRACTICE) {
                PracticeHubScreen(
                    onOpenSpellingSetup = {
                        navController.navigate(Routes.PRACTICE_SETUP)
                    },
                    onOpenFillBlankSetup = {
                        // اگر route جدا داری همان را بگذار
                        navController.navigate(Routes.PRACTICE_SETUP)
                    }
                )
            }

            composable(Routes.MAIN_LEARN) {
                LearnHubScreen(
                    onStartRestaurantLesson = {
                        navController.navigate(Routes.lesson())
                    }
                )
            }


            composable(Routes.SETTINGS) {
                // SettingsScreen(onBack = { navController.popBackStack() })
            }

            composable(Routes.PRACTICE_SETUP) {
                // PracticeSetupScreen قبلی‌ات
                // onStart → navigate به play
                // onBack → popBackStack
            }

            composable(
                route = Routes.LESSON,
                arguments = listOf(
                    navArgument("lessonId") {
                        type = NavType.StringType
                        defaultValue = "lesson-restaurant-en-a1-intro"
                    },
                    navArgument("difficulty") {
                        type = NavType.StringType
                        defaultValue = "EASY"
                    }
                )
            ) {
                LessonScreen(onBack = { navController.popBackStack() })
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

        }
    }
}

@Preview(
    locale = "fa",
    name = "Home RTL")
@Composable
fun previewMainShell() {
    MainShell()
}