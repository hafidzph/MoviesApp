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
import com.challenge.moviesapp.model.movie.popular.Result
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

        val itemFilm = arguments?.getParcelable<Result>("item_film")
        getDetailMovie(itemFilm!!.id)
        binding?.apply {
            backButton.setOnClickListener {
                toHome()
            }

            fabAddFav.setOnClickListener{
                addToFav(itemFilm)
            }
        }
    }

    private fun toHome(){
        findNavController().navigate(R.id.action_detailMoviesFragment_to_homeFragment2)
    }

    private fun addToFav(result: Result){
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                if(detailVM.existingMovie(result.title)){
                    lifecycleScope.launch(Dispatchers.Main) {
                        Toast.makeText(requireContext(), "The movie is already in favorites", Toast.LENGTH_SHORT).show()
                    }
                }else{
                    detailVM.addToFavourite(result)
                    lifecycleScope.launch(Dispatchers.Main) {
                        Toast.makeText(requireContext(), "Successfully added to favorites", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception){
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