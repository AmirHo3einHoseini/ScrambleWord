package com.example.scrambleword.di.database

import android.content.Context
import androidx.room.Room
import com.example.scrambleword.data.local.MIGRATION_1_2
import com.example.scrambleword.data.local.WordDatabase
import com.example.scrambleword.data.local.dao.WordDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import kotlin.jvm.java


@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {


    @Provides
    @Singleton
    fun provideWordDatabase(
        @ApplicationContext context: Context
    ): WordDatabase {
        return Room.databaseBuilder(
            context,
            WordDatabase::class.java,
            "word_scramble.db"
        )
            .addMigrations(MIGRATION_1_2)
            .build()

    }

    @Provides
    @Singleton
    fun provideWordDao(database: WordDatabase): WordDao {
        return database.wordDao()
    }



}