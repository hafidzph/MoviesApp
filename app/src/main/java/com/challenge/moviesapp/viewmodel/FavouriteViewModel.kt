package com.challenge.moviesapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.challenge.moviesapp.data.local.dao.FavouriteDao
import com.challenge.moviesapp.data.local.datastore.UserPreferences
import com.challenge.moviesapp.model.movie.favourite.FavouriteMovie
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class FavouriteViewModel @Inject constructor(private val userPreferences: UserPreferences, private val favDao: FavouriteDao): ViewModel() {
    private val _favMovie = MutableLiveData<List<FavouriteMovie>>()
    val favMovie: LiveData<List<FavouriteMovie>> = _favMovie

    fun getFavMovie() = viewModelScope.launch(Dispatchers.IO) {
        try {
            _favMovie.postValue(favDao.getAllFavMovie(userPreferences.getUserId()!!))
        } catch (e: Exception){
            Log.d("Error fav", "Error post fav movie")
        }
    }

    fun deleteFav(favMovie: FavouriteMovie) = viewModelScope.launch(Dispatchers.IO) {
        favDao.delete(favMovie.id)
        val currentList = _favMovie.value.orEmpty().toMutableList()
        currentList.remove(favMovie)
        _favMovie.postValue(currentList)
    }
}