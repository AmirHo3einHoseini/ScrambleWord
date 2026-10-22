package com.example.scrambleword.di

import android.content.Context
import com.example.scrambleword.data.remote.exercise.ExerciseApi
import com.example.scrambleword.data.remote.exercise.LessonApi
import com.example.scrambleword.data.remote.exercise.LocalJsonExerciseApi
import com.example.scrambleword.data.remote.exercise.LocalJsonLessonApi
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideGson(): Gson = Gson()

    @Provides
    @Singleton
    fun provideExerciseApi(
        @ApplicationContext context: Context,
        gson: Gson
    ): ExerciseApi {
        return LocalJsonExerciseApi(context, gson)
    }


    @Provides
    @Singleton
    fun provideLessonApi(
        @ApplicationContext context: Context,
        gson: Gson
    ): LessonApi = LocalJsonLessonApi(context, gson)
}