package com.challenge.moviesapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.challenge.moviesapp.R
import com.challenge.moviesapp.data.local.dao.FavouriteDao
import com.challenge.moviesapp.data.local.dao.UserDao
import com.challenge.moviesapp.data.local.datastore.LanguagePreferences
import com.challenge.moviesapp.data.local.datastore.UserPreferences
import com.challenge.moviesapp.model.user.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val auth: FirebaseAuth,
                                        private val userDao: UserDao,
                                        private val userPreferences: UserPreferences,
                                        private val languagePreferences: LanguagePreferences,
                                        private val favouriteDao: FavouriteDao): ViewModel() {
    fun saveUserIdAndUsername(id: Int, username: String) = viewModelScope.launch(Dispatchers.IO) {
        userPreferences.saveId(id)
        userPreferences.saveUsername(username)
    }

    fun saveLanguage(lang: String) = viewModelScope.launch(Dispatchers.IO) { languagePreferences.setLanguage(lang) }

    fun signIn(email: String, password: String, navController: NavController){
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    updateUI(user, navController)
                } else {
                    updateUI(null, navController)
                }
            }
    }

    private fun updateUI(user: FirebaseUser?, navController: NavController) {
        if(user != null){
            navController.navigate(R.id.action_loginFragment_to_homeFragment2)
        }
    }

    suspend fun getUserByEmailAndPassword(email: String, password: String): User? {
        return withContext(Dispatchers.IO) {
            userDao.getUserByEmailAndPassword(email, password)
        }
    }
}
