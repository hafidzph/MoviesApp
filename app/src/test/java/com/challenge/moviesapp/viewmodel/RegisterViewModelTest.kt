package com.challenge.moviesapp.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.navigation.NavController
import com.challenge.moviesapp.data.local.dao.UserDao
import com.challenge.moviesapp.utils.DataDummy
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class RegisterViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var userDao: UserDao

    @Mock
    private lateinit var auth: FirebaseAuth

    @Mock
    private lateinit var signUpTask: Task<AuthResult>

    @Mock
    private lateinit var navController: NavController

    private lateinit var registerVM: RegisterViewModel

    private val dummyUser = DataDummy.generateUserDummy()

    @Before
    fun setUp() {
        Dispatchers.setMain(TestCoroutineDispatcher())
        registerVM = RegisterViewModel(userDao, auth)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `when insertUser Successful`() = runBlocking {
        registerVM.insertUser(dummyUser)

        Mockito.verify(userDao).insertUser(dummyUser)
    }

    @Test
    fun `when checkIfUserExists True`()= runBlocking{
        Mockito.`when`(userDao.checkIfUserExists(dummyUser.username, dummyUser.email)).thenReturn(1)

        val result = registerVM.checkIfUserExists(dummyUser.username, dummyUser.email)

        assertEquals(true, result)
    }

    @Test
    fun `when checkIfUserExists False`()= runBlocking{
        Mockito.`when`(userDao.checkIfUserExists(dummyUser.username, dummyUser.email)).thenReturn(0)

        val result = registerVM.checkIfUserExists(dummyUser.username, dummyUser.email)

        assertEquals(false, result)
    }

    @Test
    fun `when SignIn is Successful`(){
        val email = "test@gmail.com"
        val password = "test"

        Mockito.`when`(auth.createUserWithEmailAndPassword(email, password)).thenReturn(signUpTask)
        Mockito.`when`(signUpTask.isSuccessful).thenReturn(true)

        registerVM.signUpUser(email, password, navController)

        Mockito.verify(auth).createUserWithEmailAndPassword(email, password)
    }

    @Test
    fun `when SignIn is Unsuccessful`(){
        val email = "test@gmail.com"
        val password = "test"

        Mockito.`when`(auth.createUserWithEmailAndPassword(email, password)).thenReturn(signUpTask)
        Mockito.`when`(signUpTask.isSuccessful).thenReturn(false)

        registerVM.signUpUser(email, password, navController)

        Mockito.verify(auth).createUserWithEmailAndPassword(email, password)
    }
}