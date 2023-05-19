package com.challenge.moviesapp.model.movie.toprated


import com.google.gson.annotations.SerializedName

@Suppress("unused", "RedundantSuppression")
data class ResponseDataTopRated(
    @SerializedName("results")
    val results: List<ResultTopRated>
)