package com.challenge.moviesapp.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.challenge.moviesapp.databinding.FavMovieItemBinding
import com.challenge.moviesapp.model.movie.favourite.FavouriteMovie

class FavouriteAdapter(private var listFav: List<FavouriteMovie>): RecyclerView.Adapter<FavouriteAdapter.ViewHolder>() {
    var onClick: ((FavouriteMovie) -> Unit)? = null

    inner class ViewHolder(private var binding: FavMovieItemBinding): RecyclerView.ViewHolder(binding.root){
        @SuppressLint("SetTextI18n")
        fun bind(favMovie: FavouriteMovie){
            val imgUrl = "https://image.tmdb.org/t/p/w500/${favMovie.img}"
            binding.apply {
                tvMoviesTitle.text = favMovie.title
                tvReleaseDate.text = favMovie.date
                extendedFab.setOnClickListener {
                    onClick?.invoke(favMovie)
                }
            }
            Glide.with(itemView).load(imgUrl).into(binding.imgFilm)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = FavMovieItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listFav.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(listFav[position])
}