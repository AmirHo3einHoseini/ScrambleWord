package com.example.scrambleword.core.data.preferences

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey


object PreferencesKeys{

    val APP_LANGUAGE= stringPreferencesKey("app_language")
    val PACK_EN_A1_SEEDED = booleanPreferencesKey("pack_en_a1_core_v1_seeded")
}
