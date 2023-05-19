package com.challenge.moviesapp.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.challenge.moviesapp.R
import com.challenge.moviesapp.adapter.FavouriteAdapter
import com.challenge.moviesapp.databinding.FragmentFavouriteMovieBinding
import com.challenge.moviesapp.viewmodel.FavouriteViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
@Suppress("unused", "RedundantSuppression")
class FavouriteMovieFragment : Fragment() {
    private var binding: FragmentFavouriteMovieBinding? = null
    private val favVM: FavouriteViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavouriteMovieBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.back?.setOnClickListener {
            toHome()
        }
        getFavMovie()
    }

    private fun getFavMovie(){
        favVM.getFavMovie()
        favVM.favMovie.observe(viewLifecycleOwner) { favMovie ->
            binding!!.rvFav.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            binding!!.rvFav.adapter = FavouriteAdapter(favMovie)
        }
    }

    private fun toHome(){
        findNavController().navigate(R.id.action_favouriteMovieFragment_to_homeFragment2)
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}