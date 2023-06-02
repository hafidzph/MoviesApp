package com.challenge.moviesapp.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.challenge.moviesapp.data.local.dao.FavouriteDao
import com.challenge.moviesapp.data.local.datastore.UserPreferences
import com.challenge.moviesapp.utils.DataDummy
import com.challenge.moviesapp.utils.getOrAwaitValue
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
class FavouriteViewModelTest{
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var userPreferences: UserPreferences

    @Mock
    private lateinit var favouriteDao: FavouriteDao

    private lateinit var favouriteVM: FavouriteViewModel

    @Before
    fun setUp(){
        favouriteVM = FavouriteViewModel(userPreferences, favouriteDao)
    }

    @Test
    fun `when getFavMovie Successful`() = runBlocking {
        val favDummy = DataDummy.generateDummyFavEntity()

        Mockito.`when`(userPreferences.getUserId()).thenReturn(1)
        Mockito.`when`(favouriteDao.getAllFavMovie(userPreferences.getUserId()!!)).thenReturn(favDummy)
        favouriteVM.getFavMovie()

        val value = favouriteVM.favMovie.getOrAwaitValue()

        assertEquals(favDummy, value)
    }

    @Test
    fun `when getFavMovie Error`() = runBlocking {
        Mockito.`when`(userPreferences.getUserId()).thenReturn(1)
        Mockito.`when`(favouriteDao.getAllFavMovie(userPreferences.getUserId()!!)).thenReturn(null)
        favouriteVM.getFavMovie()

        val value = favouriteVM.favMovie.getOrAwaitValue()

        assertNull(value)
    }
}