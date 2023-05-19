package com.challenge.moviesapp.adapter

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.challenge.moviesapp.R
import com.challenge.moviesapp.databinding.MovieItemBinding
import com.challenge.moviesapp.model.movie.nowplaying.ResultNowPlaying

class MovieNowPlayingAdapter(private var listNowPlaying: List<ResultNowPlaying>): RecyclerView.Adapter<MovieNowPlayingAdapter.ViewHolder>() {
    inner class ViewHolder(private var binding: MovieItemBinding): RecyclerView.ViewHolder(binding.root){
        @SuppressLint("SetTextI18n")
        fun bind(nowPlaying: ResultNowPlaying){
            val imgUrl = "https://image.tmdb.org/t/p/w500/${nowPlaying.posterPath}"
            val userScore = kotlin.math.round(nowPlaying.voteAverage * 10).toInt()
            binding.apply {
                tvTitle.text = nowPlaying.title
                tvDate.text = nowPlaying.releaseDate
                tvUserScore.text = "$userScore%"
                cvFilm.setOnClickListener {
                    val bundle = Bundle()
                    bundle.putInt("id", nowPlaying.id)
                    it.findNavController().navigate(R.id.action_homeFragment2_to_detailMoviesFragment, bundle)
                }
            }
            Glide.with(itemView).load(imgUrl).into(binding.ivFilm)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = MovieItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listNowPlaying.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(listNowPlaying[position])
}