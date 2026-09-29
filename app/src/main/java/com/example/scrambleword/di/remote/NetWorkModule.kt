package com.example.scrambleword.di.remote

import com.example.scrambleword.data.remote.api.FreeDictionaryApi
import com.example.scrambleword.data.remote.api.WiktApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton


private const val FREE_DICT_BASE_URL = "https://api.dictionaryapi.dev/"

@Module
@InstallIn(SingletonComponent::class)
object NetWorkModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient{
        val logging= HttpLoggingInterceptor().apply {
            level=HttpLoggingInterceptor.Level.BODY
        }
        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    @Named("freeDict")
    fun provideFreeDictRetrofit(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.dictionaryapi.dev/")  // ← همین Base URL
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideFreeDictionaryApi(
        @Named("freeDict") retrofit: Retrofit
    ): FreeDictionaryApi {
        return retrofit.create(FreeDictionaryApi::class.java)
    }



    @Provides
    @Singleton
    @Named("wikt")
    fun provideWiktRetrofit(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.wiktapi.dev/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideWiktApi(
        @Named("wikt") retrofit: Retrofit
    ): WiktApi {
        return retrofit.create(WiktApi::class.java)
    }


}