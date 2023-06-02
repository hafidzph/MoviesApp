package com.challenge.moviesapp.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.challenge.moviesapp.data.local.dao.FavouriteDao
import com.challenge.moviesapp.data.local.datastore.LanguagePreferences
import com.challenge.moviesapp.data.local.datastore.UserPreferences
import com.challenge.moviesapp.data.remote.service.APIMovieService
import com.challenge.moviesapp.model.movie.favourite.FavouriteMovie
import com.challenge.moviesapp.utils.DataDummy
import com.challenge.moviesapp.utils.getOrAwaitValue
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
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
import java.lang.Exception

@RunWith(MockitoJUnitRunner::class)
class DetailMoviesViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var languagePreferences: LanguagePreferences

    @Mock
    private lateinit var service: APIMovieService

    @Mock
    private lateinit var favouriteDao: FavouriteDao

    @Mock
    private lateinit var userPreferences: UserPreferences

    private lateinit var detailVM: DetailMoviesViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(TestCoroutineDispatcher())
        detailVM = DetailMoviesViewModel(languagePreferences, service, favouriteDao, userPreferences)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `when getMovieDetail Successful`() = runBlocking {
        val dummyDetail = DataDummy.generateDummyMovieDetail()
        val id = 594767

        Mockito.`when`(languagePreferences.getLanguage()).thenReturn(flowOf("en-EN"))
        Mockito.`when`(service.getDetailMovie(id, "d4e032a78d32940d67d6b1e0a21d82ca", languagePreferences.getLanguage().first())).thenReturn(dummyDetail)
        detailVM.getMovieDetail(id)

        val detailValue = detailVM.detailMovie.getOrAwaitValue()

        assertEquals(dummyDetail, detailValue)
    }

    @Test
    fun `when getMovieDetail Error`() = runBlocking {
        val id = 594767

        Mockito.`when`(languagePreferences.getLanguage()).thenReturn(flowOf("en-EN"))
        Mockito.`when`(service.getDetailMovie(id, "d4e032a78d32940d67d6b1e0a21d82ca", languagePreferences.getLanguage().first())).thenReturn(null)
        detailVM.getMovieDetail(id)

        val detailValue = detailVM.detailMovie.getOrAwaitValue()

        assertNull(detailValue)
    }

    @Test
    fun `when checkFavoriteStatus Return True`() = runBlocking {
        val favMovie = DataDummy.generateDummyFavEntityInsert()

        Mockito.`when`(userPreferences.getUserId()).thenReturn(1)
        Mockito.`when`(favouriteDao.getFavoriteMovieCount(userPreferences.getUserId()!!, favMovie.id)).thenReturn(1)
        detailVM.checkFavoriteStatus(favMovie)

        assertEquals(true, detailVM.isFavorite.getOrAwaitValue())
    }

    @Test
    fun `when checkFavoriteStatus Return False`() = runBlocking {
        val favMovie = DataDummy.generateDummyFavEntityInsert()

        Mockito.`when`(userPreferences.getUserId()).thenReturn(1)
        Mockito.`when`(favouriteDao.getFavoriteMovieCount(userPreferences.getUserId()!!, favMovie.id)).thenReturn(0)
        detailVM.checkFavoriteStatus(favMovie)

        assertEquals(false, detailVM.isFavorite.getOrAwaitValue())
    }

    @Test
    fun `when addToFavourites Return True`() = runBlocking {
        val favMovie = DataDummy.generateDummyFavEntityInsert()

        Mockito.`when`(favouriteDao.insert(favMovie)).thenReturn(Unit)
        detailVM.addToFavorites(favMovie)

        assertEquals(true, detailVM.isFavorite.getOrAwaitValue())
    }

    @Test
    fun `when removeFromFavourites Return False`() = runBlocking {
        val favMovie = DataDummy.generateDummyFavEntityInsert()

        Mockito.`when`(favouriteDao.delete(favMovie.id)).thenReturn(favMovie.id)
        detailVM.removeFromFavorites(favMovie)

        assertEquals(false, detailVM.isFavorite.getOrAwaitValue())
    }
}