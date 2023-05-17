package com.challenge.moviesapp.model.movie.popular


import com.google.gson.annotations.SerializedName

data class ResponsePopularMovie(
    @SerializedName("results")
    val results: List<ResultPopular>
)