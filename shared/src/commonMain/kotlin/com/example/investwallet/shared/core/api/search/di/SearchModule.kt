package com.example.investwallet.shared.core.api.search.di

import com.example.investwallet.shared.core.api.search.JSONSearchApi
import com.example.investwallet.shared.core.api.search.SearchApi
import com.example.investwallet.shared.core.api.search.repository.RepositorySearch
import com.example.investwallet.shared.di.EngineSDK
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton
import kotlin.native.concurrent.ThreadLocal

internal val searchModule = DI.Module(
    name = "SearchModule",
    init = {
        bind<JSONSearchApi>() with singleton {
            SearchApi(
                httpClient = instance()
            )
        }

        bind<RepositorySearch>() with singleton {
            RepositorySearch(
                searchApi = instance()
            )
        }
    }
)


@ThreadLocal
object SearchModule{
    val apiSearchRepository: RepositorySearch
        get() = EngineSDK.di.instance()
}

val EngineSDK.repoSearch: SearchModule
    get() = SearchModule