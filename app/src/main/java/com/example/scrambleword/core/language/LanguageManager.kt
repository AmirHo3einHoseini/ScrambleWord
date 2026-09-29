package com.example.scrambleword.core.language

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.scrambleword.core.data.preferences.UserPreferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class LanguageManager @Inject constructor(
    private val preferencesDataStore: UserPreferencesDataStore
) {
    val currentLanguage: Flow<AppLanguage> = preferencesDataStore.appLanguageCode.map { code ->
        AppLanguage.fromCode(code)
    }

    private val languageKey = stringPreferencesKey("app_language")


    suspend fun setLanguage(language: AppLanguage) {
        preferencesDataStore.setAppLanguageCode(language.code)
        val localeList = LocaleListCompat.forLanguageTags(language.code)
        AppCompatDelegate.setApplicationLocales(localeList)
    }


    fun getCurrentLanguageSync(): AppLanguage {
        return runBlocking {
            currentLanguage.first()
        }
    }

    fun applyLanguage(language: AppLanguage) {
        val localeList = LocaleListCompat.forLanguageTags(language.code)
        AppCompatDelegate.setApplicationLocales(localeList)
    }
}