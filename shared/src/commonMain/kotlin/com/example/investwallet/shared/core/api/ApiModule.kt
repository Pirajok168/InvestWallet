package com.example.investwallet.shared.core.api

import com.example.investwallet.shared.core.api.search.di.searchModule
import org.kodein.di.DI

val apiModule = DI.Module(name = "ApiModule"){
    importAll(
        searchModule
    )
}