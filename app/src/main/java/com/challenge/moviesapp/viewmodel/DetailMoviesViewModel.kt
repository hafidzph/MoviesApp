package com.challenge.moviesapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.challenge.moviesapp.BuildConfig
import com.challenge.moviesapp.data.local.dao.FavouriteDao
import com.challenge.moviesapp.data.local.datastore.LanguagePreferences
import com.challenge.moviesapp.data.local.datastore.UserPreferences
import com.challenge.moviesapp.data.remote.service.APIMovieService
import com.challenge.moviesapp.model.movie.detail.ResponseMovieDetail
import com.challenge.moviesapp.model.movie.favourite.FavouriteMovie
import com.challenge.moviesapp.model.movie.popular.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class DetailMoviesViewModel @Inject constructor(private val langPrefs: LanguagePreferences,
                                                private val movieService: APIMovieService,
                                                private val favDao: FavouriteDao,
                                                private val userPreferences: UserPreferences): ViewModel() {
    private val _detailMovie = MutableLiveData<ResponseMovieDetail>()
    val detailMovie: LiveData<ResponseMovieDetail> = _detailMovie

    fun getMovieDetail(id: Int){
        viewModelScope.launch {
            langPrefs.getLanguage().collect {
                val langCode = if (it == "id") "id-ID" else "en-EN"
                try {
                    val response = movieService.getDetailMovie(id, "d4e032a78d32940d67d6b1e0a21d82ca", langCode)
                    _detailMovie.postValue(response)
                }catch (e: Exception){
                    if(BuildConfig.DEBUG) Log.d("Error Detail", e.message!!)
                }
            }
        }
    }

    suspend fun existingMovie(title: String): Boolean =
        withContext(viewModelScope.coroutineContext) {
            favDao.getFavouriteMovieByTitleAndUserId(title, userPreferences.getUserId()!!) > 0
        }

    fun addToFavourite(result: Result) = viewModelScope.launch {
        favDao.insert(
            FavouriteMovie(img = result.posterPath, title = result.title,
                date = result.releaseDate, userId = userPreferences.getUserId()!!)
        )
    }
}