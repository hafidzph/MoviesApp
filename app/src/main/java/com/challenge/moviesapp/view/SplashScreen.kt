package com.challenge.moviesapp.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.challenge.moviesapp.databinding.FragmentSplashScreenBinding
import com.challenge.moviesapp.viewmodel.SplashViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Locale

@AndroidEntryPoint
@SuppressLint("CustomSplashScreen")
@Suppress("unused", "RedundantSuppression")
class SplashScreen : Fragment() {
    private var binding: FragmentSplashScreenBinding? = null
    private val splashVM: SplashViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSplashScreenBinding.inflate(layoutInflater, container, false)
        return binding!!.root
    }

    override fun onStart() {
        super.onStart()
        Handler(Looper.getMainLooper()).postDelayed({
            splashVM.currentUser(findNavController())
        }, 3000)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch(Dispatchers.IO) {
            setLocale(splashVM.getLanguage())
        }
    }

    private fun setLocale(lang: String){
        val res = requireContext().resources
        val conf = res.configuration
        val locale = Locale(lang)
        Locale.setDefault(locale)
        conf.setLocale(locale)

        res.updateConfiguration(conf, res.displayMetrics)
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}