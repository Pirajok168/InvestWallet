package com.example.investwallet.shared.core.api.search

import com.example.investwallet.shared.core.api.search.entity.StockDTO

interface JSONSearchApi {


    suspend fun getFindQuotes( text: String
                              ,lang: String
                              ,type: String
                              ,exchange: String): List<StockDTO>
}