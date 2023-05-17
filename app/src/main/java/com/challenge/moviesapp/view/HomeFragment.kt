package com.challenge.moviesapp.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.challenge.moviesapp.R
import com.challenge.moviesapp.adapter.MovieAdapter
import com.challenge.moviesapp.adapter.MovieNowPlayingAdapter
import com.challenge.moviesapp.adapter.MovieTopRatedAdapter
import com.challenge.moviesapp.adapter.MovieUpcomingAdapter
import com.challenge.moviesapp.databinding.FragmentHomeBinding
import com.challenge.moviesapp.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private var binding: FragmentHomeBinding? = null
    private val homeVM: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding!!.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.apply {
            profile.setOnClickListener {
                toProfile()
            }

            lifecycleScope.launch(Dispatchers.Main) {
                showUsername.text = "Hello, ${homeVM.getUsername()}"
            }

            fav.setOnClickListener{
                toFavourite()
            }
        }
        getAllMovie()
    }

    private fun getAllMovie(){
        binding!!.apply {
            progressBar.visibility = View.VISIBLE
            nowPlaying.visibility = View.GONE
            popular.visibility = View.GONE
            topRated.visibility = View.GONE
            upcoming.visibility = View.GONE
        }
        homeVM.apply {
            getPopularMovie()
            getNowPlaying()
            getTopRated()
            getUpcomingMovie()

            moviePopular.observe(viewLifecycleOwner) { response ->
                response?.let { movieResponse ->
                    binding!!.apply {
                        progressBar.visibility = View.GONE
                        popular.visibility = View.VISIBLE
                        rvMovie.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                        rvMovie.adapter = MovieAdapter(movieResponse.results)
                    }
                }
            }

            nowPlaying.observe(viewLifecycleOwner) { response ->
                response?.let { movieNowPlayingResponse ->
                    binding!!.apply {
                        progressBar.visibility = View.GONE
                        nowPlaying.visibility = View.VISIBLE
                        rvNowPlaying.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                        rvNowPlaying.adapter = MovieNowPlayingAdapter(movieNowPlayingResponse.results)

                    }
                }
            }

            topRated.observe(viewLifecycleOwner) { response ->
                response?.let { movieTopRated ->
                    binding!!.apply {
                        progressBar.visibility = View.GONE
                        topRated.visibility = View.VISIBLE
                        rvTopRated.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                        rvTopRated.adapter = MovieTopRatedAdapter(movieTopRated.results)
                    }
                }
            }

            upcoming.observe(viewLifecycleOwner) { response ->
                response?.let { movieUpcoming ->
                    binding!!.apply {
                        progressBar.visibility = View.GONE
                        upcoming.visibility = View.VISIBLE
                        rvUpcoming.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                        rvUpcoming.adapter = MovieUpcomingAdapter(movieUpcoming.results)
                    }
                }
            }
        }
    }

    private fun toProfile(){
        findNavController().navigate(R.id.action_homeFragment2_to_updateProfileFragment)
    }

    private fun toFavourite(){
        findNavController().navigate(R.id.action_homeFragment2_to_favouriteMovieFragment)
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}