package com.example.investwallet.shared.core.api.search.repository

import com.example.investwallet.shared.core.api.search.JSONSearchApi
import com.example.investwallet.shared.core.api.search.entity.StockDTO

class RepositorySearch(
    private val searchApi: JSONSearchApi
) {

    suspend fun getFindQuotes( text: String
                                ,lang: String
                                ,type: String
                                ,exchange: String): List<StockDTO> =
        searchApi.getFindQuotes(text, lang, type, exchange)
}