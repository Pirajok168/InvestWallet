package com.example.investwallet.shared.core.api

interface IRemoteDataSource {
    suspend fun fetchTickets(): String
}