package com.example.investwallet.database

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.room.*
import com.example.investwallet.dto.converter.IUTag
import com.example.investwallet.repository.ApiRepository
import com.example.investwallet.repository.StateCollectData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import javax.inject.Inject


@Entity(tableName = "favoriteTicket")
data class FavoriteTicket(
    @PrimaryKey val id: String,
    val userOwnerId: Int,
    val logoid: String? ="",
    val base_currency_logoid: String? = null,
    val typespecs: String? = null,
    val description: String = "",
    val prefix: String? = null,
    var exchange: String = "",
    var symbol: String = "",
    var country: String = "",
    var type: String
): IUTag{
    @Ignore var price: String = ""

    override fun getURLImg(): String{
        return if (base_currency_logoid == null) {
            "https://s3-symbol-logo.tradingview.com/$logoid--big.svg"
        }else{
            "https://s3-symbol-logo.tradingview.com/$base_currency_logoid--big.svg"
        }
    }

    override fun getSymbols(): String {
        return replace(symbol)
    }

    private fun replace(str: String): String{
        var newStr = str
        newStr = newStr.replace("<em>", "")
        newStr = newStr.replace("</em>", "")
        return newStr
    }

    override fun getTag(): String {
        return if (typespecs == "etf" && prefix != null){
            "${replace(prefix)}:${replace(symbol)}".uppercase()
        }else{
            "${replace(exchange)}:${replace(symbol)}".uppercase()
        }
    }

    override fun getDescriptions(): String {
        return replace(description)
    }


}


@Entity(tableName = "user_table")
data class User(
    @PrimaryKey val id: Int,
)


data class ActiveUser(
    @Embedded val user: User,
    @Relation(
        parentColumn = "id",
        entityColumn = "userOwnerId"
    )
    val listFavoriteTicket: List<FavoriteTicket>
)
