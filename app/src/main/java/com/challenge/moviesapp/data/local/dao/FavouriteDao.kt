package com.challenge.moviesapp.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.challenge.moviesapp.model.movie.favourite.FavouriteMovie

@Dao
interface FavouriteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(favMovie: FavouriteMovie)

    @Query("SELECT * FROM favourite WHERE userId = :userId ORDER BY id DESC")
    fun getAllFavMovie(userId: Int): List<FavouriteMovie>

    @Query("DELETE FROM favourite WHERE id = :id")
    suspend fun delete(id: Int): Int

    @Query("SELECT COUNT(*) FROM favourite WHERE id = :movieId AND userId = :userId")
    suspend fun getFavoriteMovieCount(movieId: Int , userId: Int): Int

    @Query("DELETE FROM favourite")
    suspend fun deleteMovie()
}