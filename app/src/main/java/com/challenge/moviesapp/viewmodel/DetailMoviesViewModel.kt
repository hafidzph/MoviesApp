package com.challenge.moviesapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.challenge.moviesapp.data.local.dao.FavouriteDao
import com.challenge.moviesapp.data.local.datastore.LanguagePreferences
import com.challenge.moviesapp.data.local.datastore.UserPreferences
import com.challenge.moviesapp.data.remote.service.APIMovieService
import com.challenge.moviesapp.model.movie.detail.ResponseMovieDetail
import com.challenge.moviesapp.model.movie.favourite.FavouriteMovie
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class DetailMoviesViewModel @Inject constructor(private val langPrefs: LanguagePreferences,
                                                private val movieService: APIMovieService,
                                                private val favDao: FavouriteDao,
                                                private val userPreferences: UserPreferences): ViewModel() {
    private val _detailMovie = MutableLiveData<ResponseMovieDetail>()
    val detailMovie: LiveData<ResponseMovieDetail> = _detailMovie

    private val _favMovie = MutableLiveData<FavouriteMovie>()
    val favMovie: LiveData<FavouriteMovie> = _favMovie

    private val _isFavorite = MutableLiveData<Boolean>()
    val isFavorite: LiveData<Boolean> = _isFavorite

    private val currentDateTime = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())

    fun getMovieDetail(id: Int){
        viewModelScope.launch(Dispatchers.IO) {
            val langCode = if (langPrefs.getLanguage().first() == "id") "id-ID" else "en-EN"
            try {
                val response = movieService.getDetailMovie(id, "d4e032a78d32940d67d6b1e0a21d82ca", langCode)
                _detailMovie.postValue(response)
                _favMovie.postValue(FavouriteMovie(id, response.posterPath, response.title, response.releaseDate, currentDateTime, userPreferences.getUserId()!!))
            }catch (e: Exception){
                Log.d("Error Detail", e.message ?: "Unknown error occurred")
            }
        }
    }

    fun checkFavoriteStatus(movie: FavouriteMovie) {
        viewModelScope.launch {
            val isFav = favDao.getFavoriteMovieCount(movie.id, userPreferences.getUserId()!!)
            _isFavorite.value = isFav > 0
        }
    }

    fun addToFavorites(movie: FavouriteMovie) {
        viewModelScope.launch {
            favDao.insert(movie)
            _isFavorite.value = true
        }
    }

    fun removeFromFavorites(movie: FavouriteMovie) {
        viewModelScope.launch {
            favDao.delete(movie.id)
            _isFavorite.value = false
        }
    }
}