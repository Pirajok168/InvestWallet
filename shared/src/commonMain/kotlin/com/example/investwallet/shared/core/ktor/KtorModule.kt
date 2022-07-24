package com.example.investwallet.shared.core.ktor

import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton

internal val ktorModule = DI.Module(
    name = "KtorModule",
    init = {

        bind<HttpEngineFactory>() with singleton { HttpEngineFactory() }

        bind<HttpClient>() with singleton {

            val engine = instance<HttpEngineFactory>().createEngine()

            HttpClient(engine) {
                install(Logging) {
                    logger = Logger.SIMPLE
                    level = LogLevel.ALL
                }

                install(ContentNegotiation) {
                    json(Json {
                        prettyPrint = true
                        isLenient = true
                    })
                }

                defaultRequest {
                    host = "symbol-search.tradingview.com"
                    url {
                        protocol = URLProtocol.HTTPS
                    }
                }
            }
        }
    }
)