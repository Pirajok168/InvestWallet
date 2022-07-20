package com.example.investwallet.shared.core.ktor

import io.ktor.client.engine.*
import io.ktor.client.engine.darwin.*

actual class HttpEngineFactory {
    actual fun createEngine(): HttpClientEngineFactory<HttpClientEngineConfig> {
        return Darwin
    }
}