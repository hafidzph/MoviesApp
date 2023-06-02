package com.challenge.moviesapp.viewmodel

import android.app.Application
import android.widget.Toast
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import com.challenge.moviesapp.data.local.dao.UserDao
import com.challenge.moviesapp.data.local.datastore.UserPreferences
import com.challenge.moviesapp.utils.getOrAwaitValue
import com.google.firebase.auth.FirebaseAuth
import io.mockk.verify
import org.junit.Assert.*
import com.challenge.moviesapp.R
import com.challenge.moviesapp.utils.DataDummy
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class UpdateProfileViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var auth: FirebaseAuth

    @Mock
    private lateinit var  userDao: UserDao

    @Mock
    private lateinit var userPreferences: UserPreferences

    @Mock
    private lateinit var app: Application

    @Mock
    private lateinit var navController: NavController

    private lateinit var updateVM: UpdateProfileViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(TestCoroutineDispatcher())
        MockitoAnnotations.initMocks(this)
        updateVM = UpdateProfileViewModel(auth, userDao, userPreferences, app)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `test signOut`() {
        updateVM.signOut(navController)

        Mockito.verify(auth).signOut()
        Mockito.verify(navController).navigate(R.id.action_updateProfileFragment_to_loginFragment)

        assertEquals(Unit, updateVM.logout.getOrAwaitValue())
    }

    @Test
    fun `when getUser is successful`() = runBlocking {
        val userId = 1
        Mockito.`when`(userPreferences.getUserId()).thenReturn(userId)
        val expectedUser = DataDummy.generateUserDummy()
        Mockito.`when`(userDao.getUser(userId)).thenReturn(expectedUser)

        val result = updateVM.getUser()

        Mockito.verify(userPreferences).getUserId()
        Mockito.verify(userDao).getUser(userId)
        assertEquals(expectedUser, result)
    }


    @Test
    fun `getUsername is successful`() = runBlocking {
        val expectedUsername = "test_user"
        Mockito.`when`(userPreferences.getUsername()).thenReturn(expectedUsername)

        val result = updateVM.getUsername()

        Mockito.verify(userPreferences).getUsername()
        assertEquals(expectedUsername, result)
    }

    @Test
    fun `successful clear username and id user`() = runBlocking {
        updateVM.clear()

        Mockito.verify(userPreferences).clearUserIdAndUsername()
    }
}