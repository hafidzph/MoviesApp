package com.challenge.moviesapp.viewmodel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.challenge.moviesapp.R
import com.challenge.moviesapp.data.local.dao.UserDao
import com.challenge.moviesapp.data.local.datastore.UserPreferences
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UpdateProfileViewModel @Inject constructor(private val auth: FirebaseAuth,
                                                private val userDao: UserDao,
                                                private val userPreferences: UserPreferences,
                                                private val app: Application): ViewModel() {
    private val _logout = MutableLiveData<Unit>()
    val logout: LiveData<Unit> = _logout

    fun signOut(navController: NavController){
        auth.signOut()
        navController.navigate(R.id.action_updateProfileFragment_to_loginFragment)
        _logout.postValue(Unit)
    }

    fun updateProfile(username: String, full_name: String, birth_date: String, address: String, navController: NavController) =
        viewModelScope.launch {
            val countUsername = userDao.isUsernameExists(username)
            if(countUsername > 0){
                Toast.makeText(app.applicationContext, "Username already exists, please try another one", Toast.LENGTH_SHORT).show()
                navController.navigate(R.id.action_updateProfileFragment_to_homeFragment2)
            }else{
                Toast.makeText(app.applicationContext, "Update profile successful", Toast.LENGTH_SHORT).show()
                userDao.updateProfile(username, full_name, birth_date, address, userPreferences.getUserId()!!)
                userPreferences.saveUsername(username)
            }
        }

    fun clear() = viewModelScope.launch { userPreferences.clearUserIdAndUsername() }
}