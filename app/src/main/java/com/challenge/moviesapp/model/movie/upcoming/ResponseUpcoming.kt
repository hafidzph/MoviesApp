package com.challenge.moviesapp.model.movie.upcoming


import com.google.gson.annotations.SerializedName

data class ResponseUpcoming(
    @SerializedName("results")
    val results: List<ResultUpcoming>
)