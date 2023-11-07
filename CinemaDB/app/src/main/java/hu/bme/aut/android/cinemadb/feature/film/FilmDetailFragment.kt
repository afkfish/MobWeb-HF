package hu.bme.aut.android.cinemadb.feature.film

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import hu.bme.aut.android.cinemadb.databinding.FragmentFilmDetailBinding
import hu.bme.aut.android.cinemadb.model.film.Film
import hu.bme.aut.android.cinemadb.model.film.FilmViewModel


class FilmDetailFragment : Fragment() {
    private lateinit var binding: FragmentFilmDetailBinding

    private lateinit var film: Film

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFilmDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        findNavController()
            .currentBackStackEntry
            ?.savedStateHandle
            ?.getLiveData<Int>("film")
            ?.observe(viewLifecycleOwner) { index ->
                viewModels<FilmViewModel>().value.filmResponse.observe(viewLifecycleOwner) {
                    film = it.first?.body?.films?.get(index)!!
                    loadCinema()
                }
            }
    }

    private fun loadCinema() {
        binding.filmTitle.text = film.name
        binding.filmReleaseDate.text = film.releaseYear
        binding.filmFormat.text = film.attributeIds.joinToString(", ")

        Glide.with(this)
            .load(film.posterLink)
            .into(binding.filmImage)
    }

    companion object {
        const val film = "film"
    }
}