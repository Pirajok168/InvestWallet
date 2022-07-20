package com.example.investwallet.shared.core.api

import com.example.investwallet.shared.core.repository.ApiRepository
import com.example.investwallet.shared.di.EngineSDK
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton
import kotlin.native.concurrent.ThreadLocal

internal val apiModule = DI.Module(
    name = "ApiModule",
    init = {
        bind<IRemoteDataSource>() with singleton {
            KtorSearchTicket(
                httpClient = instance(),
            )
        }
        bind<ApiRepository>() with singleton {
            ApiRepository(
                apiSource = instance()
            )
        }
    }
)

@ThreadLocal
object RepoApiModule{
    val repo: ApiRepository
        get() = EngineSDK.di.instance()
}


val EngineSDK.apiModule: RepoApiModule
    get() = RepoApiModule

