package com.example.scrambleword.data.seed

import android.content.Context
import com.example.scrambleword.data.local.dao.WordDao
import com.example.scrambleword.data.local.mapper.toEntityOrNull
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.InputStreamReader
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PackSeeder @Inject constructor(
    @ApplicationContext private val context: Context,
    private val wordDao: WordDao,
    private val gson: Gson
) {
    suspend fun seedEnA1CoreIfNeeded(alreadySeeded: Boolean): Boolean {
        if (alreadySeeded) return false
        return withContext(Dispatchers.IO) {
            val words = loadFromAssets()
            if (words.isEmpty()) return@withContext false
            words.forEach { wordDao.insert(it) }
            true

        }

    }


    private fun loadFromAssets(): List<com.example.scrambleword.data.local.entity.WordEntity> {
        context.assets.open("packs/en_a1_core_v1.json").use { input ->
            val type = object : TypeToken<List<PackWordDto>>() {}.type
            val dtoList: List<PackWordDto> =
                gson.fromJson(InputStreamReader(input, Charsets.UTF_8), type)

            return dtoList.mapNotNull { it.toEntityOrNull() }
        }
    }
}

