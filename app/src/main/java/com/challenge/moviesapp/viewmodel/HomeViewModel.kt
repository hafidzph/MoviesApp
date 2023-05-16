package com.challenge.moviesapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.challenge.moviesapp.BuildConfig
import com.challenge.moviesapp.data.local.datastore.UserPreferences
import com.challenge.moviesapp.data.remote.service.APIMovieService
import com.challenge.moviesapp.model.movie.popular.ResponsePopularMovie
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val userPreferences: UserPreferences,
                                        private val movieService: APIMovieService): ViewModel() {
    private val _moviePopular = MutableLiveData<ResponsePopularMovie>(null)
    val moviePopular: LiveData<ResponsePopularMovie> = _moviePopular

    fun getPopularMovie(){
        viewModelScope.launch {
            try {
                val response = movieService.getPopularMovies("d4e032a78d32940d67d6b1e0a21d82ca", 1)
                _moviePopular.postValue(response)
            }catch (e: Exception){
                if(BuildConfig.DEBUG) Log.d("Error", e.message!!)
            }
        }
    }

    suspend fun getUsername(): String? = userPreferences.getUsername()
}