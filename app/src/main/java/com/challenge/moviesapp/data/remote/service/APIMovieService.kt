package com.challenge.moviesapp.data.remote.service

import com.challenge.moviesapp.model.movie.detail.ResponseMovieDetail
import com.challenge.moviesapp.model.movie.popular.ResponsePopularMovie
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface APIMovieService {
    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("api_key") apiKey: String,
        @Query("page") page: Int
    ): ResponsePopularMovie

    @GET("movie/{id}")
    suspend fun getDetailMovie(
        @Path("id") id: Int,
        @Query("api_key") apiKey: String,
        @Query("language") language: String
    ): ResponseMovieDetail
}