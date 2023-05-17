package com.challenge.moviesapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.challenge.moviesapp.BuildConfig
import com.challenge.moviesapp.data.local.datastore.UserPreferences
import com.challenge.moviesapp.data.remote.service.APIMovieService
import com.challenge.moviesapp.model.movie.nowplaying.ResponseNowPlaying
import com.challenge.moviesapp.model.movie.popular.ResponsePopularMovie
import com.challenge.moviesapp.model.movie.toprated.ResponseDataTopRated
import com.challenge.moviesapp.model.movie.upcoming.ResponseUpcoming
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val userPreferences: UserPreferences,
                                        private val movieService: APIMovieService): ViewModel() {
    private val _moviePopular = MutableLiveData<ResponsePopularMovie>()
    val moviePopular: LiveData<ResponsePopularMovie> = _moviePopular

    private val _nowPlaying = MutableLiveData<ResponseNowPlaying>()
    val nowPlaying: LiveData<ResponseNowPlaying> = _nowPlaying

    private val _topRated = MutableLiveData<ResponseDataTopRated>()
    val topRated: LiveData<ResponseDataTopRated> = _topRated

    private val _upcoming = MutableLiveData<ResponseUpcoming>()
    val upcoming: LiveData<ResponseUpcoming> = _upcoming

    fun getPopularMovie(){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = movieService.getPopularMovies("d4e032a78d32940d67d6b1e0a21d82ca", 1)
                _moviePopular.postValue(response)
            }catch (e: Exception){
                if (BuildConfig.DEBUG) Log.d("Error", e.message!!)
            }
        }
    }

    fun getNowPlaying(){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = movieService.getNowPlaying("d4e032a78d32940d67d6b1e0a21d82ca")
                _nowPlaying.postValue(response)
            }catch (e: Exception){
                if (BuildConfig.DEBUG) Log.d("Error", e.message!!)
            }
        }
    }

    fun getTopRated(){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = movieService.getTopRated("d4e032a78d32940d67d6b1e0a21d82ca")
                _topRated.postValue(response)
            }catch (e: Exception){
                if (BuildConfig.DEBUG) Log.d("Error", e.message!!)
            }
        }
    }

    fun getUpcomingMovie(){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = movieService.getUpcoming("d4e032a78d32940d67d6b1e0a21d82ca")
                _upcoming.postValue(response)
            }catch (e: Exception){
                if (BuildConfig.DEBUG) Log.d("Error", e.message!!)
            }
        }
    }

    suspend fun getUsername(): String? = userPreferences.getUsername()
}