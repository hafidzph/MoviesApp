package com.challenge.moviesapp.data.remote.config

import com.challenge.moviesapp.data.remote.service.APIMovieService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object APIConfig {
    const val  BASE_URL ="https://api.themoviedb.org/3/"

    val instance : APIMovieService by lazy {
        val retrofit= Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        retrofit.create(APIMovieService::class.java)
    }
}