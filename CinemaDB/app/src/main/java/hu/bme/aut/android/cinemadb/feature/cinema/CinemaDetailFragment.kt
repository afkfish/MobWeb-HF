package hu.bme.aut.android.cinemadb.feature.cinema

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import hu.bme.aut.android.cinemadb.databinding.FragmentCinemaDetailBinding
import hu.bme.aut.android.cinemadb.model.cinema.Cinema
import hu.bme.aut.android.cinemadb.model.cinema.CinemaViewModel


class CinemaDetailFragment : Fragment() {
    private lateinit var binding: FragmentCinemaDetailBinding

    private lateinit var cinema: Cinema

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCinemaDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        findNavController()
            .currentBackStackEntry
            ?.savedStateHandle
            ?.getLiveData<Int>("cinema")
            ?.observe(viewLifecycleOwner) { index ->
                viewModels<CinemaViewModel>().value.cinemaResponse.observe(viewLifecycleOwner) {
                    cinema = it.first?.body?.cinemas?.get(index)!!
                    loadCinema()
                }
            }
    }

    private fun loadCinema() {
        binding.cinemaName.text = cinema.displayName
        binding.cinemaInfo1.text = cinema.address

        Glide.with(this)
            .load(cinema.imageUrl)
            .into(binding.cinemaImage)
    }

    companion object {
        const val cinema = "cinema"
    }
}