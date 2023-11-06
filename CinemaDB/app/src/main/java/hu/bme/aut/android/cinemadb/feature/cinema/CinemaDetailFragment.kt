package hu.bme.aut.android.cinemadb.feature.cinema

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import hu.bme.aut.android.cinemadb.databinding.FragmentCinemaDetailBinding


class CinemaDetailFragment : Fragment() {
    private lateinit var binding: FragmentCinemaDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCinemaDetailBinding.inflate(inflater, container, false)
        return binding.root
    }
}