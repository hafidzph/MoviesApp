package com.challenge.moviesapp.model.movie.favourite

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.challenge.moviesapp.model.user.User
import kotlinx.parcelize.Parcelize

@Entity(tableName = "favourite",
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        )
    ])
@Suppress("unused", "RedundantSuppression")
@Parcelize
data class FavouriteMovie (
    @PrimaryKey(autoGenerate = true) var id: Int,
    @ColumnInfo(name = "imgLink") var img: String,
    @ColumnInfo(name = "title") var title: String,
    @ColumnInfo(name = "date") var date: String,
    @ColumnInfo(name = "userId") var userId: Int
    ): Parcelable