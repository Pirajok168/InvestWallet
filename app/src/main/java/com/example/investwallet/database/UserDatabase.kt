package com.example.investwallet.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.investwallet.database.dao.UserDao


@Database(entities = [User::class, FavoriteTicket::class], version = 1, exportSchema = false)
abstract class UserDatabase: RoomDatabase() {
    abstract fun userDao(): UserDao
}