package com.challenge.moviesapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.challenge.moviesapp.R
import com.challenge.moviesapp.data.local.datastore.LanguagePreferences
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(private val auth: FirebaseAuth,
                                          private val languagePreferences: LanguagePreferences): ViewModel() {
    fun currentUser(navController: NavController){
        val currentUser = auth.currentUser
        if(currentUser == null) navController.navigate(R.id.action_splashScreen_to_loginFragment)
        else reload(navController)
    }

    private fun reload(navController: NavController) {
        auth.currentUser?.reload()?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                navController.navigate(R.id.action_splashScreen_to_homeFragment2)
            } else {
                FirebaseAuth.getInstance().signOut()
            }
        }
    }

    suspend fun getLanguage(): String = withContext(Dispatchers.IO) { languagePreferences.getLanguage().first() }
}