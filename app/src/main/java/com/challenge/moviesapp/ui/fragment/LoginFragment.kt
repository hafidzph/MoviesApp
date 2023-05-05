package com.challenge.moviesapp.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.challenge.moviesapp.R
import com.challenge.moviesapp.data.local.datastore.UserPreferences
import com.challenge.moviesapp.data.local.entity.User
import com.challenge.moviesapp.databinding.FragmentLoginBinding
import com.challenge.moviesapp.ui.viewmodel.LoginViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginFragment : Fragment() {
    private var binding: FragmentLoginBinding? = null
    private val loginVM: LoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(layoutInflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.apply {
            btnLogin.setOnClickListener {
                val email = etEmail.text.toString().trim()
                val password = etPass.text.toString().trim()
                lifecycleScope.launch {
                    val getUser = loginVM.getUserByEmailAndPassword(email, password)
                    authLogin(email, password, getUser)
                }
            }

            daftar.setOnClickListener {
                toRegister()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    private fun toRegister(){
        findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
    }

    private fun authLogin(email: String, password: String, user: User?){
        if(email.isNotEmpty() && password.isNotEmpty() && user != null){
            loginVM.signIn(email, password, findNavController())
            loginVM.saveUserIdAndUsername(user.id, user.username)
        }else{
            Toast.makeText(context, "Email and Password is not invalid", Toast.LENGTH_SHORT).show()
        }
    }
}