package com.challenge.moviesapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.challenge.moviesapp.R
import com.challenge.moviesapp.data.local.dao.UserDao
import com.challenge.moviesapp.model.user.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(private val userDao: UserDao,
                                            private val auth: FirebaseAuth): ViewModel() {
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
                    val user = auth.currentUser
                    updateUI(user, navController)
                } else {
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