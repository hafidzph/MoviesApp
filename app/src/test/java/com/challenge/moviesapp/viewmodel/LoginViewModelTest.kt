package com.challenge.moviesapp.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.navigation.NavController
import com.challenge.moviesapp.data.local.dao.UserDao
import com.challenge.moviesapp.data.local.datastore.LanguagePreferences
import com.challenge.moviesapp.data.local.datastore.UserPreferences
import com.challenge.moviesapp.model.user.User
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
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
class LoginViewModelTest{
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var auth: FirebaseAuth

    @Mock
    private lateinit var userPreferences: UserPreferences

    @Mock
    private lateinit var languagePreferences: LanguagePreferences

    @Mock
    private lateinit var userDao: UserDao

    @Mock
    private lateinit var signInTask: Task<AuthResult>

    @Mock
    private lateinit var navController: NavController

    private lateinit var loginVM: LoginViewModel

    @Before
    fun setUp(){
        loginVM = LoginViewModel(auth, userDao, userPreferences, languagePreferences)
    }

    @Test
    fun `when Save UserId and Username Success`() = runBlocking {
        val id = 1
        val username = "test_user"

        Mockito.`when`(userPreferences.getUserId()).thenReturn(id)
        Mockito.`when`(userPreferences.getUsername()).thenReturn(username)

        loginVM.saveUserIdAndUsername(id, username)

        assertEquals(id, userPreferences.getUserId())
        assertEquals(username, userPreferences.getUsername())
    }

    @Test
    fun `when Save UserId and Username Error`() = runBlocking {
        val id = 1
        val username = "test_user"

        Mockito.`when`(userPreferences.getUserId()).thenReturn(null)
        Mockito.`when`(userPreferences.getUsername()).thenReturn(null)

        loginVM.saveUserIdAndUsername(id, username)

        assertNull(userPreferences.getUserId())
        assertNull(userPreferences.getUsername())
    }

    @Test
    fun `when SignIn is Successful`(){
        val email = "test@gmail.com"
        val password = "test"

        Mockito.`when`(auth.signInWithEmailAndPassword(email, password)).thenReturn(signInTask)
        Mockito.`when`(signInTask.isSuccessful).thenReturn(true)

        loginVM.signIn(email, password, navController)

        Mockito.verify(auth).signInWithEmailAndPassword(email, password)
    }

    @Test
    fun `when SignIn is Error`(){
        val email = "test@gmail.com"
        val password = "test"

        Mockito.`when`(auth.signInWithEmailAndPassword(email, password)).thenReturn(signInTask)
        Mockito.`when`(signInTask.isSuccessful).thenReturn(false)

        loginVM.signIn(email, password, navController)

        Mockito.verify(auth).signInWithEmailAndPassword(email, password)
    }

    @Test
    fun `when getUserByEmailAndPassword Successful`() = runBlocking {
        val email = "test@gmail.com"
        val password = "password"

        val expectedUser = User(1, "test_user", email, password, "Tester User", "22-04-2001", "Jalan Kesemek")

        Mockito.`when`(userDao.getUserByEmailAndPassword(email, password)).thenReturn(expectedUser)

        val result = loginVM.getUserByEmailAndPassword(email, password)

        assertEquals(expectedUser, result)
    }

    @Test
    fun `when getUserByEmailAndPassword Error`() = runBlocking {
        val email = "test@gmail.com"
        val password = "password"

        Mockito.`when`(userDao.getUserByEmailAndPassword(email, password))
            .thenReturn(null)

        val result = loginVM.getUserByEmailAndPassword(email, password)

        assertNull(result)
    }
}