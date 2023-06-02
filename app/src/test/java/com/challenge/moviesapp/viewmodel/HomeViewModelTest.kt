package com.challenge.moviesapp.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.challenge.moviesapp.data.local.datastore.UserPreferences
import com.challenge.moviesapp.data.remote.service.APIMovieService
import com.challenge.moviesapp.model.movie.nowplaying.ResponseNowPlaying
import com.challenge.moviesapp.model.movie.popular.ResponsePopularMovie
import com.challenge.moviesapp.model.movie.toprated.ResponseDataTopRated
import com.challenge.moviesapp.model.movie.upcoming.ResponseUpcoming
import com.challenge.moviesapp.utils.getOrAwaitValue
import io.mockk.mockk
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
class HomeViewModelTest {

        @get:Rule
        val instantExecutorRule = InstantTaskExecutorRule()

        @Mock
        private lateinit var service:APIMovieService

        @Mock
        private lateinit var userPreferences: UserPreferences

        private lateinit var viewModel: HomeViewModel

        private val apiKey = "d4e032a78d32940d67d6b1e0a21d82ca"

        @Before
        fun setUp(){
            viewModel = HomeViewModel(userPreferences, service)
        }

        //Popular Movie Test
        @Test
        fun `when Popular Movies Response Success and Data is Exist`() = runBlocking {
            val response = mockk<ResponsePopularMovie>()
            Mockito.`when`(service.getPopularMovies(apiKey, 1)).thenReturn(response)
            viewModel.getPopularMovie()

            val value = viewModel.moviePopular.getOrAwaitValue()

            assertEquals(response, value)
        }

        @Test
        fun `when Popular Movies Response Error and Data is Null`() = runBlocking {
            Mockito.`when`(service.getPopularMovies(apiKey, 1)).thenReturn(null)
            viewModel.getPopularMovie()

            val value = viewModel.moviePopular.getOrAwaitValue()

            assertNull(value)
        }

        //Now Playing Movie Test
        @Test
        fun `when Now Playing Movies Response Success and Data is Exist`() = runBlocking {
            val response = mockk<ResponseNowPlaying>()
            Mockito.`when`(service.getNowPlaying(apiKey)).thenReturn(response)
            viewModel.getNowPlaying()

            val value = viewModel.nowPlaying.getOrAwaitValue()

            assertEquals(response, value)
        }

        @Test
        fun `when Now Playing Movies Response Error and Data is Null`() = runBlocking {
            Mockito.`when`(service.getNowPlaying(apiKey)).thenReturn(null)
            viewModel.getNowPlaying()

            val value = viewModel.nowPlaying.getOrAwaitValue()

            assertNull(value)
        }

        //Top Rated Movie Test
        @Test
        fun `when Top Rated Movies Response Success and Data is Exist`() = runBlocking {
            val response = mockk<ResponseDataTopRated>()
            Mockito.`when`(service.getTopRated(apiKey)).thenReturn(response)
            viewModel.getTopRated()

            val value = viewModel.topRated.getOrAwaitValue()

            assertEquals(response, value)
        }

        @Test
        fun `when Top Rated Movies Response Error and Data is Null`() = runBlocking {
            Mockito.`when`(service.getTopRated(apiKey)).thenReturn(null)
            viewModel.getTopRated()

            val value = viewModel.topRated.getOrAwaitValue()

            assertNull(value)
        }

        //Upcoming Movie Test
        @Test
        fun `when Upcoming Movies Response Success and Data is Exist`() = runBlocking {
            val response = mockk<ResponseUpcoming>()
            Mockito.`when`(service.getUpcoming(apiKey)).thenReturn(response)
            viewModel.getUpcomingMovie()

            val value = viewModel.upcoming.getOrAwaitValue()

            assertEquals(response, value)
        }

        @Test
        fun `when Upcoming Movies Response Error and Data is Null`() = runBlocking {
            Mockito.`when`(service.getUpcoming(apiKey)).thenReturn(null)
            viewModel.getUpcomingMovie()

            val value = viewModel.upcoming.getOrAwaitValue()

            assertNull(value)
        }

        @Test
        fun `when Username From UserPreferences Exist`() = runBlocking {
            val username = "test_user"
            Mockito.`when`(userPreferences.getUsername()).thenReturn(username)

            assertEquals(username, viewModel.getUsername())
        }

        @Test
        fun `when Username From UserPreferences is Null`() = runBlocking {
            Mockito.`when`(userPreferences.getUsername()).thenReturn(null)

            assertNull(viewModel.getUsername())
        }
}