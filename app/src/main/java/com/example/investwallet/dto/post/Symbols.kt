package com.example.investwallet.dto.post

data class Symbols(
    val query: Query,
    val tickers: List<String>
)
