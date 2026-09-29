package com.example.scrambleword.data.remote.api

import com.example.scrambleword.data.remote.dto.FreeDictionaryEntryDto
import retrofit2.http.GET
import retrofit2.http.Path

interface FreeDictionaryApi {


    @GET("api/v2/entries/{language}/{word}")
    suspend fun getEntries(
        @Path("language") language: String,
        @Path("word") word: String,
    ): List<FreeDictionaryEntryDto>
}