package com.example.scrambleword.data.remote.api

import com.example.scrambleword.data.remote.dto.WiktWordResponseDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface WiktApi {

    @GET("v1/{edition}/word/{word}")
    suspend fun getWord(
        @Path("edition") edition: String = "en",
        @Path("word") word: String,
        @Query("lang") lang: String
    ): WiktWordResponseDto

}