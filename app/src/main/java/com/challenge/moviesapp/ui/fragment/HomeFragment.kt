package com.challenge.moviesapp.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.challenge.moviesapp.R
import com.challenge.moviesapp.adapter.MovieAdapter
import com.challenge.moviesapp.databinding.FragmentHomeBinding
import com.challenge.moviesapp.ui.viewmodel.HomeViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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
        }
        getAllMovie("en-EN")
    }

    private fun getAllMovie(lang: String){
        binding!!.progressBar.visibility = View.VISIBLE
        homeVM.getPopularMovie(lang)
        homeVM.moviePopular.observe(viewLifecycleOwner) { response ->
            response?.let { movieResponse ->
                binding!!.progressBar.visibility = View.GONE
                binding!!.rvMovie.layoutManager = LinearLayoutManager(requireContext(),
                    LinearLayoutManager.VERTICAL,
                    false)
                binding!!.rvMovie.adapter = MovieAdapter(movieResponse.results)
            }
        }
    }

    private fun toProfile(){
        findNavController().navigate(R.id.action_homeFragment2_to_updateProfileFragment)
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}