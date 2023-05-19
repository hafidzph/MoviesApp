package com.challenge.moviesapp.view

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
import com.challenge.moviesapp.model.user.User
import com.challenge.moviesapp.databinding.FragmentRegisterBinding
import com.challenge.moviesapp.viewmodel.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
@Suppress("unused", "RedundantSuppression")
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
                    tilUsername.error = if (username.isEmpty()) context?.getString(R.string.msg_username_empty) else null
                    tilEmail.error = if (email.isEmpty()) context?.getString(R.string.msg_email_empty) else null
                    tilPass.error = if (password.isEmpty()) context?.getString(R.string.msg_password_empty) else null
                    tilPassConf.error = if (confirmPassword.isEmpty()) context?.getString(R.string.msg_cp_empty) else null
                }else {
                    tilUsername.error = null
                    tilEmail.error = null
                    tilPass.error = null
                    lifecycleScope.launch(Dispatchers.Main) {
                        val isExist = withContext(Dispatchers.IO){ registerVM.checkIfUserExists(username, email) }
                        if (password != confirmPassword) {
                            tilPassConf.error = context?.getString(R.string.msg_matchpsw)
                        } else {
                            tilPassConf.error = null
                            if (isExist) {
                                Toast.makeText(requireContext(), context?.getString(R.string.msg_existing), Toast.LENGTH_LONG).show()
                            } else {
                                if(isValidEmail(email)) {
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
                                    Toast.makeText(
                                        requireContext(),
                                        R.string.msg_success_register,
                                        Toast.LENGTH_LONG
                                    ).show()
                                }else{
                                    tilEmail.error = context?.getString(R.string.msg_email_invalid)
                                }
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

    private fun isValidEmail(email: String): Boolean {
        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\$"
        return email.matches(emailRegex.toRegex())
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}