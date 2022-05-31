package com.example.investwallet.dto

import com.example.investwallet.dto.converter.IUTag

data class QuoteDTO(
    val country: String?,
    val description: String,
    var exchange: String,
    val provider_id: String,
    var symbol: String,
    val type: String,
    val typespecs: List<String>?,
    val logoid: String,
    val `base-currency-logoid`: String? = null,
    val prefix: String? = null
): IUTag{

    override fun getURLImg(): String{
        return if (`base-currency-logoid` == null) {
            "https://s3-symbol-logo.tradingview.com/$logoid--big.svg"
        }else{
            "https://s3-symbol-logo.tradingview.com/$`base-currency-logoid`--big.svg"
        }
    }

    override fun getSymbols(): String {
        TODO("Not yet implemented")
    }


    private fun replace(str: String): String{
        var newStr = str
        newStr = newStr.replace("<em>", "")
        newStr = newStr.replace("</em>", "")
        return newStr
    }

    override fun getTag(): String {
        return if (typespecs?.firstOrNull() == "etf" && prefix != null){
            "${replace(prefix)}:${replace(symbol)}".uppercase()
        }else{
            "${replace(exchange)}:${replace(symbol)}".uppercase()
        }
    }

    override fun getDescriptions(): String {
        return "${replace(description)}"
    }
}
