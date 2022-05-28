package com.example.investwallet.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.example.investwallet.database.ActiveUser
import com.example.investwallet.database.FavoriteTicket
import com.example.investwallet.database.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow


@Dao
interface UserDao {

    @Transaction
    @Query("SELECT * FROM user_table")
    fun getUser(): LiveData<ActiveUser>

    @Transaction
    @Insert
    suspend fun insertActiveUser(user: ActiveUser){
        insert(user.user)
        user.listFavoriteTicket.forEach {
            insertFavoriteTicket(ticket = it)
        }

    }


    @Insert
    suspend fun insert(user: User)


    @Insert
    suspend fun insertFavoriteTicket(ticket: FavoriteTicket)
}