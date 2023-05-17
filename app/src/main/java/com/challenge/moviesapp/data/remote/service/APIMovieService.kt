package com.challenge.moviesapp.data.remote.service

import com.challenge.moviesapp.model.movie.detail.ResponseMovieDetail
import com.challenge.moviesapp.model.movie.nowplaying.ResponseNowPlaying
import com.challenge.moviesapp.model.movie.popular.ResponsePopularMovie
import com.challenge.moviesapp.model.movie.toprated.ResponseDataTopRated
import com.challenge.moviesapp.model.movie.upcoming.ResponseUpcoming
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

    @GET("movie/now_playing")
    suspend fun getNowPlaying(
        @Query("api_key") apiKey: String
    ): ResponseNowPlaying

    @GET("movie/top_rated")
    suspend fun getTopRated(
        @Query("api_key") apiKey: String
    ): ResponseDataTopRated

    @GET("movie/upcoming")
    suspend fun getUpcoming(
        @Query("api_key") apiKey: String
    ): ResponseUpcoming
}