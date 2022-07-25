package com.example.investwallet.shared.core.api.search.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class StockDTO(
    val country: String? = null,
    val description: String? = null,
    var exchange: String? = null,
    val provider_id: String? = null,
    var symbol: String? = null,
    val type: String? = null,
    val typespecs: List<String>? = null,
    val logoid: String? = null,
    @SerialName("base-currency-logoid") val base_currency_logoid: String? = null,
    val prefix: String? = null,
    val currency_code: String? = null,
){
    fun getURLImg(): String{
        return if (base_currency_logoid == null) {
            "https://s3-symbol-logo.tradingview.com/$logoid--big.svg"
        }else{
            "https://s3-symbol-logo.tradingview.com/$base_currency_logoid--big.svg"
        }
    }

    private fun replace(str: String): String{
        var newStr = str
        newStr = newStr.replace("<em>", "")
        newStr = newStr.replace("</em>", "")
        return newStr
    }

    fun getDescriptions(): String {
        return "${replace(description!!)}"
    }
}