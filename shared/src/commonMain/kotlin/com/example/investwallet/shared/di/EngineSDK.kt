package com.example.investwallet.shared.di

import com.example.investwallet.shared.core.featureModule
import org.kodein.di.DI
import org.kodein.di.DirectDI
import org.kodein.di.direct
import kotlin.native.concurrent.ThreadLocal


@ThreadLocal
object EngineSDK {
    internal val di: DirectDI
        get() = requireNotNull(_di)
    private var _di: DirectDI? = null

    init {
        if (_di != null) {
            _di = null
        }

        val direct = DI {
            importAll(
                featureModule,
            )
        }.direct

        _di = direct
    }

}