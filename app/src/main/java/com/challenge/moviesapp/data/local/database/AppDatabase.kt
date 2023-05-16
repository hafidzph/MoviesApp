package com.challenge.moviesapp.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.challenge.moviesapp.data.local.dao.FavouriteDao
import com.challenge.moviesapp.data.local.dao.UserDao
import com.challenge.moviesapp.model.movie.favourite.FavouriteMovie
import com.challenge.moviesapp.model.user.User

@Database(entities = [User::class, FavouriteMovie::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun favDao(): FavouriteDao
}