package com.challenge.moviesapp.model.movie.nowplaying


import com.google.gson.annotations.SerializedName

data class ResponseNowPlaying(
    @SerializedName("results")
    val results: List<ResultNowPlaying>
)