package com.challenge.moviesapp.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.challenge.moviesapp.data.local.datastore.LanguagePreferences
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class SplashViewModelTest{
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var auth: FirebaseAuth

    @Mock
    private lateinit var languagePreferences: LanguagePreferences

    private lateinit var splashVM: SplashViewModel

    @Before
    fun setUp(){
        splashVM = SplashViewModel(auth, languagePreferences)
    }

    @Test
    fun `when getLanguage() value is filled`() = runBlocking {
        Mockito.`when`(languagePreferences.getLanguage()).thenReturn(flowOf("en"))

        val result = splashVM.getLanguage()

        assertEquals("en", result)
    }

    @Test
    fun `when getLanguage() value is unfilled`() = runBlocking {
        Mockito.`when`(languagePreferences.getLanguage()).thenReturn(flowOf(""))

        val result = splashVM.getLanguage()

        assertEquals("", result)
    }
}