package com.challenge.moviesapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.challenge.moviesapp.model.user.User

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)

    @Query("SELECT COUNT(*) FROM user WHERE username = :username")
    suspend fun isUsernameExists(username: String): Int

    @Query("SELECT * FROM user WHERE email = :email AND password = :password")
    suspend fun getUserByEmailAndPassword(email: String, password: String): User?

    @Query("SELECT * FROM user WHERE id = :id")
    suspend fun getUser(id: Int): User?

    @Query("SELECT COUNT(*) FROM user WHERE username = :username OR email = :email")
    suspend fun checkIfUserExists(username: String, email: String): Int

    @Query("UPDATE user SET username = :username, full_name = :full_name, birth_date = :birth_date, address = :address WHERE id = :id")
    suspend fun updateProfile(username: String, full_name: String, birth_date: String, address: String, id: Int): Int

    @Query("DELETE FROM user")
    suspend fun deleteUser()
}