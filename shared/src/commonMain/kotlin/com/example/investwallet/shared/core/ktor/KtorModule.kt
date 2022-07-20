package com.example.investwallet.shared.core.ktor

import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.http.*
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
                /*install(Logging) {
                    logger = Logger.SIMPLE
                    level = LogLevel.ALL
                }*/

                /*install(JsonFeature) {
                    serializer = KotlinxSerializer()
                }
*/
                defaultRequest {
                    host = "ktor.io"
                    url {
                        protocol = URLProtocol.HTTPS
                    }
                }
            }
        }
    }
)