package com.example.investwallet.shared.core.api

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.serialization.json.Json

class KtorSearchTicket(
    private val httpClient: HttpClient,
    val json: Json
): IRemoteDataSource {
    override suspend fun fetchTickets(): String {
        val httpRequest = httpClient.get {
            url{
                protocol = URLProtocol.HTTPS
                path("docs/welcome.html")
            }
        }
        return httpRequest.bodyAsText()
    }
}