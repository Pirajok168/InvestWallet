package com.example.investwallet.database

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.example.investwallet.dto.converter.IUTag



@Entity(tableName = "favoriteTicket")
data class FavoriteTicket(
    @PrimaryKey val id: Int,
    val userOwnerId: Int,
    val logoid: String ="",
    val base_currency_logoid: String? = null,
    val typespecs: String? = null,
    val description: String = "",
    val prefix: String? = null,
    var exchange: String = "",
    var symbol: String = "",
): IUTag{
    override fun getURLImg(): String{
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

    override fun getTag(): String {
        return ""
    /* return if (typespecs?.firstOrNull() == "etf" && prefix != null){
            "${replace(prefix)}:${replace(symbol)}".uppercase()
        }else{
            "${replace(exchange)}:${replace(symbol)}".uppercase()
        }*/
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
