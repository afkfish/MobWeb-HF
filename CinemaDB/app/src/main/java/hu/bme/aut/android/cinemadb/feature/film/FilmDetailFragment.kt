package hu.bme.aut.android.cinemadb.feature.film

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import hu.bme.aut.android.cinemadb.databinding.FragmentFilmDetailBinding


class FilmDetailFragment : Fragment() {
    private lateinit var binding: FragmentFilmDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFilmDetailBinding.inflate(inflater, container, false)
        return binding.root
    }
}