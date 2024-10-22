package hu.bme.aut.android.cinemadb.feature.cinema

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import hu.bme.aut.android.cinemadb.MainActivity
import hu.bme.aut.android.cinemadb.R
import hu.bme.aut.android.cinemadb.databinding.FragmentCinemaListBinding
import hu.bme.aut.android.cinemadb.model.cinema.CinemaViewModel
import hu.bme.aut.android.cinemadb.model.filmEvent.FilmEventViewModel

/**
 * A fragment representing a list of Items.
 */
class CinemaListFragment : Fragment(), CinemaListRecyclerViewAdapter.OnCinemaSelectedListener {
    private lateinit var binding: FragmentCinemaListBinding
    private lateinit var adapter: CinemaListRecyclerViewAdapter

    private lateinit var cinemaViewModel: CinemaViewModel
    private lateinit var filmEventViewModel: FilmEventViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = CinemaListRecyclerViewAdapter(this)
        cinemaViewModel = viewModels<CinemaViewModel>().value
        filmEventViewModel = viewModels<FilmEventViewModel>().value
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCinemaListBinding.inflate(inflater, container, false)
        binding.list.layoutManager = LinearLayoutManager(context)
        binding.list.adapter = adapter

        (requireActivity() as MainActivity).let {
            it.cinemaViewModel.filteredList.observe(it, adapter::updateCinemas)
        }

        return binding.root
    }

    override fun onCinemaSelected(position: Int) {
        val cinemaId = cinemaViewModel.filteredList.value?.get(position)?.id
        (activity as MainActivity).searchItem.collapseActionView()
        filmEventViewModel.loadResponseForCinema(cinemaId!!)
        findNavController().navigate(R.id.action_cinemaListFragment_to_cinemaDetailFragment)
        findNavController().currentBackStackEntry?.savedStateHandle?.set(CinemaDetailFragment.cinema, cinemaId)
    }
}