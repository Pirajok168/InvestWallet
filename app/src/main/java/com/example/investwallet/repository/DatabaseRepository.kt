package com.example.investwallet.repository

import com.example.investwallet.database.ActiveUser
import com.example.investwallet.database.FavoriteTicket
import com.example.investwallet.database.UserDatabase
import javax.inject.Inject

class DatabaseRepository @Inject constructor(
    databaseUserDatabase: UserDatabase,
) {
    private val dao = databaseUserDatabase.userDao()

    val listFavoriteTicket = dao.getLisFavorite()

    suspend fun insertActiveUser(user: ActiveUser){
        dao.insertActiveUser(user)
    }

    suspend fun insertFavoriteTicket(ticket: FavoriteTicket){
        dao.insertFavoriteTicket(ticket)
    }

    suspend fun deleteFavoriteTicket(id: String){
        dao.deleteFavoriteTicket(id)
    }

    suspend fun getFavorite(symbol: String): FavoriteTicket?{
        return dao.getTicket(symbol)
    }
}