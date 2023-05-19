package com.challenge.moviesapp.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.challenge.moviesapp.R
import com.challenge.moviesapp.databinding.FragmentLoginBinding
import com.challenge.moviesapp.model.user.User
import com.challenge.moviesapp.viewmodel.LoginViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Locale

@AndroidEntryPoint
@Suppress("unused", "RedundantSuppression")
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
                val email = etEmail.text.toString()
                val password = etPass.text.toString()
                lifecycleScope.launch(Dispatchers.IO) {
                    val getUser = loginVM.getUserByEmailAndPassword(email, password)
                    authLogin(email, password, getUser)
                }
            }

            daftar.setOnClickListener {
                toRegister()
            }

            bIng.setOnClickListener {
                setLocale("en")
            }

            bIndo.setOnClickListener {
                setLocale("id")
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
            lifecycleScope.launch(Dispatchers.Main){
                Toast.makeText(context, R.string.msg_login_invalid, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setLocale(lang: String){
        val res = requireContext().resources
        val conf = res.configuration
        val locale = Locale(lang)
        Locale.setDefault(locale)
        conf.setLocale(locale)

        res.updateConfiguration(conf, res.displayMetrics)

        loginVM.saveLanguage(lang)
        refreshFragment()
    }

    private fun refreshFragment(){
        val refreshIntent = requireActivity().intent
        requireActivity().finish()
        startActivity(refreshIntent)
    }
}