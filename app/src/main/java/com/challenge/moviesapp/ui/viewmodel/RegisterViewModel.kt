package com.challenge.moviesapp.ui.viewmodel

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.constraintlayout.widget.Constraints.TAG
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.challenge.moviesapp.R
import com.challenge.moviesapp.data.local.dao.UserDao
import com.challenge.moviesapp.data.local.database.AppDatabase
import com.challenge.moviesapp.data.local.entity.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RegisterViewModel(private val app: Application): AndroidViewModel(app) {
    private val userDao: UserDao = AppDatabase.getInstance(app).userDao()
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()

    fun insertUser(user: User) = viewModelScope.launch { userDao.insertUser(user) }

    suspend fun checkIfUserExists(username: String, email: String): Boolean {
        return withContext(Dispatchers.IO) {
            userDao.checkIfUserExists(username, email) > 0
        }
    }

    fun signUpUser(email: String, password: String, navController: NavController){
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "createUserWithEmail:success")
                    val user = auth.currentUser
                    updateUI(user, navController)
                } else {
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(app.applicationContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                    updateUI(null, navController)
                }
            }
    }

    private fun updateUI(user: FirebaseUser?, navController: NavController) {
        if(user != null){
            navController.navigate(R.id.action_registerFragment_to_loginFragment)
        }
    }
}