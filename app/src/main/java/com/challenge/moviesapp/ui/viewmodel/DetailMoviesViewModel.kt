package com.challenge.moviesapp.ui.viewmodel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.challenge.moviesapp.data.local.datastore.LanguagePreferences
import com.challenge.moviesapp.data.remote.config.APIConfig
import com.challenge.moviesapp.data.remote.model.ResponseMovieDetail
import com.challenge.moviesapp.data.remote.model.ResponsePopularMovie
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailMoviesViewModel(private val app: Application): AndroidViewModel(app) {
    private val _detailMovie = MutableLiveData<ResponseMovieDetail>(null)
    val detailMovie: LiveData<ResponseMovieDetail> = _detailMovie
    private val langPrefs = LanguagePreferences(app.applicationContext)

    fun getMovieDetail(id: Int){
        viewModelScope.launch {
            langPrefs.getLanguage(app).collect {
                val langCode = if (it == "id") "id-ID" else "en-EN"
                APIConfig.instance.getDetailMovie(id, "d4e032a78d32940d67d6b1e0a21d82ca", langCode)
                    .enqueue(object : Callback<ResponseMovieDetail> {
                        override fun onResponse(
                            call: Call<ResponseMovieDetail>,
                            response: Response<ResponseMovieDetail>
                        ) {
                            if (response.isSuccessful) _detailMovie.postValue(response.body())
                        }

                        override fun onFailure(call: Call<ResponseMovieDetail>, t: Throwable) {
                            println(t.message)
                        }
                    })
            }
        }
    }
}