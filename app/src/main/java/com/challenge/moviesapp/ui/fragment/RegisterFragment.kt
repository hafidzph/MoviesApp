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
import com.challenge.moviesapp.data.local.entity.User
import com.challenge.moviesapp.databinding.FragmentRegisterBinding
import com.challenge.moviesapp.ui.viewmodel.RegisterViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RegisterFragment : Fragment() {
    private var binding: FragmentRegisterBinding? = null
    private val registerVM: RegisterViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegisterBinding.inflate(layoutInflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.apply {
            login.setOnClickListener {
                toLogin()
            }

            btnRegis.setOnClickListener {
                val username = etUsername.text.toString()
                val email = etEmail.text.toString()
                val password = etPass.text.toString()
                val confirmPassword = etPassConf.text.toString()

                if(password.isEmpty() || confirmPassword.isEmpty() || email.isEmpty() || username.isEmpty()){
                    tilUsername.error = if (username.isEmpty()) "Username tidak boleh kosong" else null
                    tilEmail.error = if (email.isEmpty()) "Email tidak boleh kosong" else null
                    tilPass.error = if (password.isEmpty()) "Password tidak boleh kosong" else null
                    tilPassConf.error = if (confirmPassword.isEmpty()) "Konfirmasi Password tidak boleh kosong" else null
                }else {
                    tilUsername.error = null
                    tilEmail.error = null
                    tilPass.error = null
                    lifecycleScope.launch {
                        val isExist = withContext(Dispatchers.IO){ registerVM.checkIfUserExists(username, email) }
                        if (password != confirmPassword) {
                            tilPassConf.error = "Password tidak sesuai"
                        } else {
                            tilPassConf.error = null
                            if (isExist) {
                                Toast.makeText(requireContext(), "Username atau email sudah terdaftar", Toast.LENGTH_LONG).show()
                            } else {
                                withContext(Dispatchers.IO) {
                                    registerVM.insertUser(
                                        User(
                                            username = username,
                                            email = email,
                                            password = password,
                                            full_name = "",
                                            birth_date = "",
                                            address = ""
                                        )
                                    )
                                    registerVM.signUpUser(email, password, findNavController())
                                }
                                Toast.makeText(requireContext(), "Akun berhasil didaftarkan", Toast.LENGTH_LONG).show()
                            }
                        }
                    }
                }
            }
        }
    }

    private fun toLogin(){
        findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
    }


}