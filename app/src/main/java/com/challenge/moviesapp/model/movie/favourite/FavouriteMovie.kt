package com.challenge.moviesapp.model.movie.favourite

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.challenge.moviesapp.model.user.User

@Entity(tableName = "favourite",
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        )
    ])
data class FavouriteMovie (
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    @ColumnInfo(name = "imgLink") var img: String,
    @ColumnInfo(name = "title") var title: String,
    @ColumnInfo(name = "date") var date: String,
    @ColumnInfo(name = "userId") var userId: Int
    )