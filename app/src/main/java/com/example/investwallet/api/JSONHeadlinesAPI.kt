package com.example.investwallet.api

import com.example.investwallet.dto.converter.newsDtoItem

import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface JSONHeadlinesAPI {

    @Headers("Origin: https://ru.tradingview.com")
    @GET("headlines/?client=web")
    suspend fun getHeadlines(
        @Query("category") category: String,
        @Query("lang") lang: String,
        @Query("symbol") symbol: String
    ): List<newsDtoItem>

}