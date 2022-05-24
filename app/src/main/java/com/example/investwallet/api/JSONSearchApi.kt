package com.example.investwallet.api

import com.example.investwallet.dto.QuoteDTO
import retrofit2.http.GET
import retrofit2.http.Query



interface JSONSearchApi {
    @GET("/symbol_search/?exchange=&hl=1&domain=production")
    suspend fun getFindQuotes(@Query("text") text: String
                              , @Query("lang") lang: String
                              , @Query("type") type: String): List<QuoteDTO>

}