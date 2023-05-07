package com.challenge.moviesapp.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.challenge.moviesapp.R
import com.challenge.moviesapp.data.local.datastore.LanguagePreferences
import com.challenge.moviesapp.databinding.FragmentSplashScreenBinding
import com.challenge.moviesapp.ui.viewmodel.SplashViewModel
import com.google.firebase.crashlytics.FirebaseCrashlytics
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.util.Locale

@SuppressLint("CustomSplashScreen")
class SplashScreen : Fragment() {
    private var binding: FragmentSplashScreenBinding? = null
    val splashVM: SplashViewModel by viewModels()
    lateinit var langPrefs: LanguagePreferences
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
        langPrefs = LanguagePreferences(requireContext())
        lifecycleScope.launchWhenStarted {
            val language = langPrefs.getLanguage(requireContext()).first()
            val locale = Locale(language)
            Locale.setDefault(locale)
            resources.configuration.setLocale(locale)
            resources.updateConfiguration(resources.configuration, resources.displayMetrics)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}