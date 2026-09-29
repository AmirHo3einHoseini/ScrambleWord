package com.example.scrambleword.di.repository

import com.example.scrambleword.data.repository.DictionaryRepositoryImpl
import com.example.scrambleword.data.repository.WordRepositoryImpl
import com.example.scrambleword.domain.repository.DictionaryRepository
import com.example.scrambleword.domain.repository.WordRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {


    @Binds
    @Singleton
    abstract fun bindWordRepository(
        impl: WordRepositoryImpl
    ): WordRepository



    @Binds
    @Singleton
    abstract fun bindDictionaryRepository(
        impl: DictionaryRepositoryImpl
    ): DictionaryRepository

}