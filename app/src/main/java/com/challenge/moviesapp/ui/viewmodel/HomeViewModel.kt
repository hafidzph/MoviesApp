package com.challenge.moviesapp.ui.viewmodel

import android.app.Application
import android.content.Context
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.challenge.moviesapp.data.local.datastore.UserPreferences
import com.challenge.moviesapp.data.remote.config.APIConfig
import com.challenge.moviesapp.data.remote.model.ResponsePopularMovie
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel(val app: Application): AndroidViewModel(app) {
    private val _moviePopular = MutableLiveData<ResponsePopularMovie>(null)
    val moviePopular: LiveData<ResponsePopularMovie> = _moviePopular
    private val userPreferences = UserPreferences(app.applicationContext)

    fun getPopularMovie(){
        APIConfig.instance.getPopularMovies("d4e032a78d32940d67d6b1e0a21d82ca", 1, "en-EN")
            .enqueue(object : Callback<ResponsePopularMovie> {
            override fun onResponse(
                call: Call<ResponsePopularMovie>,
                response: Response<ResponsePopularMovie>
            ) {
                if(response.isSuccessful) _moviePopular.postValue(response.body())
            }

            override fun onFailure(call: Call<ResponsePopularMovie>, t: Throwable) {
                Toast.makeText(app, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    suspend fun getUsername(): String? = userPreferences.getUsername()
}