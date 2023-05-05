package com.challenge.moviesapp.data.remote.service

import com.challenge.moviesapp.data.remote.model.ResponsePopularMovie
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface APIMovieService {
    @GET("movie/popular")
    fun getPopularMovies(
        @Query("api_key") apiKey: String,
        @Query("page") page: Int,
        @Query("language") language: String
    ): Call<ResponsePopularMovie>
}