package com.challenge.moviesapp.ui.viewmodel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.challenge.moviesapp.data.remote.config.APIConfig
import com.challenge.moviesapp.data.remote.model.ResponseMovieDetail
import com.challenge.moviesapp.data.remote.model.ResponsePopularMovie
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailMoviesViewModel(private val app: Application): AndroidViewModel(app) {
    private val _detailMovie = MutableLiveData<ResponseMovieDetail>(null)
    val detailMovie: LiveData<ResponseMovieDetail> = _detailMovie

    fun getMovieDetail(lang: String, id: Int){
        APIConfig.instance.getDetailMovie(id, "d4e032a78d32940d67d6b1e0a21d82ca", lang)
            .enqueue(object : Callback<ResponseMovieDetail> {
                override fun onResponse(
                    call: Call<ResponseMovieDetail>,
                    response: Response<ResponseMovieDetail>
                ) {
                    if(response.isSuccessful) _detailMovie.postValue(response.body())
                }

                override fun onFailure(call: Call<ResponseMovieDetail>, t: Throwable) {
                    println(t.message)
                }
            })
    }
}