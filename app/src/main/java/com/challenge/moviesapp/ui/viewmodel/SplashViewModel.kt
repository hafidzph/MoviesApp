package com.challenge.moviesapp.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.navigation.NavController
import com.challenge.moviesapp.R
import com.google.firebase.auth.FirebaseAuth

class SplashViewModel(private val app: Application): AndroidViewModel(app) {
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()

    fun currentUser(navController: NavController){
        val currentUser = auth.currentUser
        if(currentUser == null) navController.navigate(R.id.action_splashScreen_to_loginFragment)
        else reload(navController)
    }

    private fun reload(navController: NavController) {
        auth.currentUser?.reload()?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val user = auth.currentUser
                navController.navigate(R.id.action_splashScreen_to_homeFragment2)
            } else {
                FirebaseAuth.getInstance().signOut()
            }
        }
    }
}