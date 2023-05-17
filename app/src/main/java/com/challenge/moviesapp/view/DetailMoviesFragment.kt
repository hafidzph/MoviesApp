package com.challenge.moviesapp.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.challenge.moviesapp.BuildConfig
import com.challenge.moviesapp.R
import com.challenge.moviesapp.databinding.FragmentDetailMoviesBinding
import com.challenge.moviesapp.model.movie.nowplaying.ResultNowPlaying
import com.challenge.moviesapp.model.movie.popular.ResultPopular
import com.challenge.moviesapp.model.movie.toprated.ResultTopRated
import com.challenge.moviesapp.model.movie.upcoming.ResultUpcoming
import com.challenge.moviesapp.viewmodel.DetailMoviesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailMoviesFragment: Fragment() {
    private var binding: FragmentDetailMoviesBinding? = null
    private val detailVM: DetailMoviesViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailMoviesBinding.inflate(layoutInflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val itemPopular = arguments?.getParcelable<ResultPopular>("itemPopular")
        val itemNowPlaying = arguments?.getParcelable<ResultNowPlaying>("itemNowPlaying")
        val itemTopRated = arguments?.getParcelable<ResultTopRated>("itemTopRated")
        val itemUpcoming = arguments?.getParcelable<ResultUpcoming>("itemUpcoming")

        when{
            itemPopular != null -> getDetailMovie(itemPopular.id)
            itemNowPlaying != null -> getDetailMovie(itemNowPlaying.id)
            itemTopRated != null -> getDetailMovie(itemTopRated.id)
            itemUpcoming != null -> getDetailMovie(itemUpcoming.id)
        }

        binding?.apply {
            backButton.setOnClickListener {
                toHome()
            }

            fabAddFav.setOnClickListener{
                (itemPopular ?: itemNowPlaying ?: itemTopRated ?: itemUpcoming)?.let {
                    addToFavorites(
                        it
                    )
                }
            }
        }
    }

    private fun toHome(){
        findNavController().navigate(R.id.action_detailMoviesFragment_to_homeFragment2)
    }

    private fun addToFavorites(item: Any) {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                when (item) {
                    is ResultPopular, is ResultTopRated, is ResultNowPlaying, is ResultUpcoming -> {
                        val title = when (item) {
                            is ResultPopular -> item.title
                            is ResultTopRated -> item.title
                            is ResultNowPlaying -> item.title
                            is ResultUpcoming -> item.title
                            else -> ""
                        }

                        if (detailVM.existingMovie(title)) {
                            lifecycleScope.launch(Dispatchers.Main) {
                                Toast.makeText(
                                    requireContext(),
                                    "The movie is already in favorites",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        } else {
                            detailVM.addToFavourite(item)
                            lifecycleScope.launch(Dispatchers.Main) {
                                Toast.makeText(
                                    requireContext(),
                                    "Successfully added to favorites",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                if (BuildConfig.DEBUG) Log.d("Error adding favorites", e.message!!)
            }
        }
    }


    @SuppressLint("SetTextI18n")
    private fun getDetailMovie(id: Int){
        detailVM.getMovieDetail(id)
        detailVM.detailMovie.observe(viewLifecycleOwner) { response ->
            response?.let { movieResponse ->
                val imgUrl = "https://image.tmdb.org/t/p/w500/${movieResponse.backdropPath}"
                binding?.movieTitle?.text = "${movieResponse.title} (${movieResponse.releaseDate.substring(0,4)})"
                binding?.overviewContent?.text = movieResponse.overview
                Glide.with(this).load(imgUrl).into(binding!!.movieImg)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}