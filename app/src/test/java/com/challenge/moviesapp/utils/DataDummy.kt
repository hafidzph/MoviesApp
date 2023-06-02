package com.challenge.moviesapp.utils

import com.challenge.moviesapp.model.movie.detail.ResponseMovieDetail
import com.challenge.moviesapp.model.movie.favourite.FavouriteMovie
import com.challenge.moviesapp.model.user.User

object DataDummy {
        fun generateDummyFavEntity(): List<FavouriteMovie> {
            val favList = ArrayList<FavouriteMovie>()

            for (i in 0..10) {
                val fav = FavouriteMovie(
                    id = i,
                    img = "https://image.tmdb.org/t/p/w500/",
                    title = "Title $i",
                    date = "2023-05-30",
                    date_added = "2023-05-30",
                    userId = 1
                )
                favList.add(fav)
            }

            return favList
        }

    fun generateDummyFavEntityInsert(): FavouriteMovie {
        return FavouriteMovie(
            id = 1,
            img = "https://image.tmdb.org/t/p/w500/",
            title = "Title Fav",
            date = "2023-05-30",
            date_added = "2023-05-30",
            userId = 1
        )
    }

    fun generateDummyMovieDetail(): ResponseMovieDetail {
        return ResponseMovieDetail(
            backdropPath = "https://image.tmdb.org/t/p/w500/nDxJJyA5giRhXx96q1sWbOUjMBI.jpg",
            posterPath = "https://image.tmdb.org/t/p/w500/A3ZbZsmsvNGdprRi2lKgGEeVLEH.jpg",
            overview = "",
            releaseDate = "2023-06-01",
            title = "Shazam! Collection"
        )
    }

    fun generateUserDummy(): User {
        return User(
            1,
            "test",
            "test@gmail.com",
            "test123",
            "tester 123",
            "22-12-2001",
            "Jalan Alaska"
        )
    }
}