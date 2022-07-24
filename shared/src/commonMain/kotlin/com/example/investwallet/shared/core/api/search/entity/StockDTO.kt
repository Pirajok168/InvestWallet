package com.example.investwallet.shared.core.api.search.entity

import kotlinx.serialization.Serializable


@Serializable
data class StockDTO(
    val country: String?,
    val description: String? = null,
    var exchange: String? = null,
    val provider_id: String? = null,
    var symbol: String? = null,
    val type: String? = null,
    val typespecs: List<String>? = null,
    val logoid: String? = null,
    val `base-currency-logoid`: String? = null,
    val prefix: String? = null,
    val currency_code: String? = null,
)