package com.example.investwallet.dto

data class QuoteDTO(
    val country: String?,
    val description: String,
    var exchange: String,
    val provider_id: String,
    var symbol: String,
    val type: String,
    val typespecs: List<String>,
    val logoid: String
){

    val tag: String
        get() = "${replace(exchange)}:${replace(symbol)}"


    val tagHttp:String
        get() = "${replace(exchange)}-${replace(symbol)}".uppercase()


    val getDescription: String
        get() = "${replace(description)}"

    private fun replace(str: String): String{
        var newStr = str
        newStr = newStr.replace("<em>", "")
        newStr = newStr.replace("</em>", "")
        return newStr
    }
}
