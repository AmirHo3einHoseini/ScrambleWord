package com.example.scrambleword.core.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = "user_preferences"
)

@Singleton
class UserPreferencesDataStore @Inject constructor(
    @ApplicationContext private val context: Context
) {

    val appLanguageCode: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[PreferencesKeys.APP_LANGUAGE] ?: "fa"
    }

    suspend fun setAppLanguageCode(code: String) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.APP_LANGUAGE] = code
        }
    }

    suspend fun isEnA1PackSeeded(): Boolean {
        return context.dataStore.data
            .map { prefs -> prefs[PreferencesKeys.PACK_EN_A1_SEEDED] == true }
            .first()
    }

    suspend fun setEnA1PackSeeded(value: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[PreferencesKeys.PACK_EN_A1_SEEDED] = value
        }
    }
}