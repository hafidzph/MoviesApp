package com.challenge.moviesapp.model.movie.detail


import com.google.gson.annotations.SerializedName

data class ResponseMovieDetail(
    @SerializedName("backdrop_path")
    val backdropPath: String,
    @SerializedName("overview")
    val overview: String,
    @SerializedName("release_date")
    val releaseDate: String,
    @SerializedName("title")
    val title: String
)