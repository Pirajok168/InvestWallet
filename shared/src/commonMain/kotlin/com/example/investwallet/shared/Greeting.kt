package com.example.investwallet.shared

import io.ktor.client.*


class Greeting {



    fun greeting(): String {
        return "Hello, ${Platform().platform}!"
    }
}

