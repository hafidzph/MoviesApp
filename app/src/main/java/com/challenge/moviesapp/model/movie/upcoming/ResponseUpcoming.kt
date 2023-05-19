package com.challenge.moviesapp.model.movie.upcoming


import com.google.gson.annotations.SerializedName

@Suppress("unused", "RedundantSuppression")
data class ResponseUpcoming(
    @SerializedName("results")
    val results: List<ResultUpcoming>
)