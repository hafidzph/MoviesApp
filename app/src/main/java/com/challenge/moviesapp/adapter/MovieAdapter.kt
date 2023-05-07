package com.challenge.moviesapp.adapter

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.challenge.moviesapp.R
import com.challenge.moviesapp.data.remote.model.Result
import com.challenge.moviesapp.databinding.MovieItemBinding
import com.challenge.moviesapp.ui.fragment.HomeFragment

class MovieAdapter(var listFilm: List<Result>): RecyclerView.Adapter<MovieAdapter.ViewHolder>() {
    inner class ViewHolder(private var binding: MovieItemBinding): RecyclerView.ViewHolder(binding.root){
        @SuppressLint("SetTextI18n")
        fun bind(detailFilm: Result, id: Int){
            val imgUrl = "https://image.tmdb.org/t/p/w500/${detailFilm.posterPath}"
            val userScore = kotlin.math.round(detailFilm.voteAverage * 10).toInt()
            binding.apply {
                tvMoviesTitle.text = detailFilm.title
                tvReleaseDate.text = detailFilm.releaseDate
                tvUserScore.text = "$userScore%"
            }
            Glide.with(itemView).load(imgUrl).into(binding.imgFilm)

            binding.cvFilm.setOnClickListener {
                val bundle = Bundle()
                bundle.putInt("id", id)
                it.findNavController().navigate(R.id.action_homeFragment2_to_detailMoviesFragment, bundle)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = MovieItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listFilm.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(listFilm[position], listFilm[position].id)
}