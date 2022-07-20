package com.example.investwallet.shared.core.repository

import com.example.investwallet.shared.core.api.IRemoteDataSource

class ApiRepository(
    private val apiSource: IRemoteDataSource
) {
    suspend fun fetchTickers() = apiSource.fetchTickets()
}