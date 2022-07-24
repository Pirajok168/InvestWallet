package com.example.investwallet.shared.core.api.search

import com.example.investwallet.shared.core.api.search.entity.StockDTO
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class SearchApi(
    private val httpClient: HttpClient
): JSONSearchApi {
    override suspend fun getFindQuotes(
        text: String,
        lang: String,
        type: String,
        exchange: String
    ): List<StockDTO> {
        val httpRequest = httpClient.get{
            url {
                contentType(ContentType.Application.Json)
                path("/symbol_search/?hl=1&domain=production")
                parameters.append("text", text)
                parameters.append("lang", lang)
                parameters.append("type", type)
                parameters.append("exchange", exchange)
            }
        }
        return Json{
            ignoreUnknownKeys = true
        }.decodeFromString<List<StockDTO>>(httpRequest.bodyAsText())
    }

}