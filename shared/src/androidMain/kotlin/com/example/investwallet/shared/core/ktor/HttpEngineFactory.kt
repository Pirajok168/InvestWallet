package com.example.investwallet.shared.core.ktor

import io.ktor.client.engine.*
import io.ktor.client.engine.okhttp.*

actual class HttpEngineFactory actual constructor() {
    actual fun createEngine(): HttpClientEngineFactory<HttpClientEngineConfig> {
        return OkHttp
    }
}