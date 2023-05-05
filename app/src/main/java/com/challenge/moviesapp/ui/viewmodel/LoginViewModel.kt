package com.challenge.moviesapp.ui.viewmodel

import android.app.Application
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.constraintlayout.widget.Constraints.TAG
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.challenge.moviesapp.R
import com.challenge.moviesapp.data.local.dao.UserDao
import com.challenge.moviesapp.data.local.database.AppDatabase
import com.challenge.moviesapp.data.local.datastore.UserPreferences
import com.challenge.moviesapp.data.local.entity.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginViewModel(val app: Application): AndroidViewModel(app) {
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val userDao: UserDao = AppDatabase.getInstance(app).userDao()
    private val userPreferences = UserPreferences(app.applicationContext)

    private val _user = MutableLiveData<FirebaseUser?>()
    val user: LiveData<FirebaseUser?> = _user

    fun signIn(email: String, password: String, navController: NavController){
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "signInWithEmail:success")
                    val user = auth.currentUser
                    _user.value = user
                    updateUI(user, navController)
                } else {
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    _user.value = null
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

    fun saveUserIdAndUsername(id: Int, username: String) = viewModelScope.launch {
        userPreferences.saveId(id)
        userPreferences.saveUsername(username)
    }
}
