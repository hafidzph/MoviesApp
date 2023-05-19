package com.challenge.moviesapp.model.movie.detail


import com.google.gson.annotations.SerializedName

@Suppress("unused", "RedundantSuppression")
data class ResponseMovieDetail(
    @SerializedName("backdrop_path")
    val backdropPath: String,
    @SerializedName("poster_path")
    val posterPath: String,
    @SerializedName("overview")
    val overview: String,
    @SerializedName("release_date")
    val releaseDate: String,
    @SerializedName("title")
    val title: String
)