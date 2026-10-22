package com.example.scrambleword.app

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.core.os.LocaleListCompat
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.example.scrambleword.app.navigations.AppNavGraph
import com.example.scrambleword.core.language.LanguageManager
import com.example.scrambleword.core.ui.theme.WordScrambleTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var languageManager: LanguageManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
        val initialLanguage = languageManager.getCurrentLanguageSync()
        languageManager.applyLanguage(initialLanguage)
        setContent {
            val viewModel: MainViewModel = hiltViewModel()
            WordScrambleApp(languageManager)
        }


    }


    @Composable
    fun WordScrambleApp(languageManager: LanguageManager) {
        val currentLanguage by languageManager.currentLanguage.collectAsStateWithLifecycle(
            initialValue = languageManager.getCurrentLanguageSync()
        )
        val navController = rememberNavController()

        CompositionLocalProvider(
            LocalLayoutDirection provides currentLanguage.layoutDirection
        ) {
            WordScrambleTheme {
                Surface(modifier = Modifier.fillMaxSize()) {

                    MainShell(navController = navController)
                }
            }
        }

    }
}