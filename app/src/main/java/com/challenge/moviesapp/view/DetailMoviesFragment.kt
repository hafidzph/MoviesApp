package com.challenge.moviesapp.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.challenge.moviesapp.R
import com.challenge.moviesapp.databinding.FragmentDetailMoviesBinding
import com.challenge.moviesapp.model.movie.favourite.FavouriteMovie
import com.challenge.moviesapp.viewmodel.DetailMoviesViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
@Suppress("unused", "RedundantSuppression")
class DetailMoviesFragment: Fragment() {
    private var binding: FragmentDetailMoviesBinding? = null
    private val detailVM: DetailMoviesViewModel by viewModels()
    private lateinit var favMovie: FavouriteMovie

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailMoviesBinding.inflate(layoutInflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val getMovieId = arguments?.getInt("id")!!
        getDetailMovie(getMovieId)
        checkIsFav()
        checkFavStatus()

        binding?.apply {
            backButton.setOnClickListener {
                toHome()
            }

            fabAddFav.setOnClickListener {
                val isFav = detailVM.isFavorite.value
                addToFav(isFav)
            }
        }
    }

    private fun addToFav(isFav: Boolean?){
        detailVM.favMovie.observe(viewLifecycleOwner) {
            if (isFav == true) {
                detailVM.removeFromFavorites(it)
                Toast.makeText(context, R.string.msg_fav_delete, Toast.LENGTH_SHORT).show()
            } else {
                detailVM.addToFavorites(it)
                Toast.makeText(context, R.string.msg_fav_input, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun checkFavStatus(){
        detailVM.favMovie.observe(viewLifecycleOwner){
            detailVM.checkFavoriteStatus(it)
        }
    }

    private fun checkIsFav(){
        detailVM.isFavorite.observe(viewLifecycleOwner) { isFav ->
            if (isFav) {
                binding?.fabAddFav?.setImageResource(R.drawable.baseline_favorite_24)
            } else {
                binding?.fabAddFav?.setImageResource(R.drawable.baseline_favorite_border_24)
            }
        }
    }

    private fun toHome(){
        findNavController().navigateUp()
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