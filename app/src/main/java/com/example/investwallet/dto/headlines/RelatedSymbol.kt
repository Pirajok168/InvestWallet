package com.example.investwallet.dto.headlines

data class RelatedSymbol(
    val logoid: String?,
    val symbol: String
){
    fun getURLImg(): String{
        return if (logoid != null)
            "https://s3-symbol-logo.tradingview.com/$logoid--big.svg"
        else{
            ""
        }
    }
}