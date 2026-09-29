package com.example.scrambleword.app

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scrambleword.core.data.preferences.UserPreferencesDataStore
import com.example.scrambleword.data.seed.PackSeeder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val packSeeder: PackSeeder,
    private val dataStore: UserPreferencesDataStore
) : ViewModel() {


    init {
        seedPackIfNeeded()
    }

    private fun seedPackIfNeeded() {
        viewModelScope.launch {
            val alreadySeeded = dataStore.isEnA1PackSeeded()
            val didSeed = packSeeder.seedEnA1CoreIfNeeded(alreadySeeded)
            if (didSeed) {
                dataStore.setEnA1PackSeeded(true)
            }
        }

    }


}