package com.challenge.moviesapp.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.challenge.moviesapp.R
import com.challenge.moviesapp.adapter.MovieAdapter
import com.challenge.moviesapp.databinding.FragmentDetailMoviesBinding
import com.challenge.moviesapp.ui.viewmodel.DetailMoviesViewModel

class DetailMoviesFragment : Fragment() {
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
        val id = requireArguments().getInt("id")
        getDetailMovie(id)
        binding?.backButton?.setOnClickListener {
            findNavController().navigate(R.id.action_detailMoviesFragment_to_homeFragment2)
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
}