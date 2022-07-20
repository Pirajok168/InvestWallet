package com.example.investwallet.shared.core

import com.example.investwallet.shared.core.api.apiModule
import com.example.investwallet.shared.core.ktor.ktorModule
import org.kodein.di.DI

val featureModule = DI.Module(name = "FeatureModule"){
    importAll(
        ktorModule,
        apiModule,
    )
}