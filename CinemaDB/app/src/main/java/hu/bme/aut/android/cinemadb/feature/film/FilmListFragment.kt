package hu.bme.aut.android.cinemadb.feature.film

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
import hu.bme.aut.android.cinemadb.databinding.FragmentFilmListBinding
import hu.bme.aut.android.cinemadb.model.film.FilmViewModel

/**
 * A fragment representing a list of Items.
 */
class FilmListFragment : Fragment(), FilmListRecyclerViewAdapter.OnFilmSelectedListener {
    private lateinit var binding: FragmentFilmListBinding
    private lateinit var adapter: FilmListRecyclerViewAdapter

    private lateinit var filmViewModel: FilmViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = FilmListRecyclerViewAdapter(this)
        filmViewModel = viewModels<FilmViewModel>().value
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFilmListBinding.inflate(inflater, container, false)
        binding.list.layoutManager = LinearLayoutManager(context)
        binding.list.adapter = adapter

        (requireActivity() as MainActivity).let {
            it.filmViewModel.filteredList.observe(it, adapter::updateFilms)
        }

        return binding.root
    }

    override fun onFilmSelected(position: Int) {
        (activity as MainActivity).searchItem.collapseActionView()
        findNavController().navigate(R.id.action_filmListFragment_to_filmDetailFragment)
        findNavController().currentBackStackEntry?.savedStateHandle?.set(FilmDetailFragment.film, position)
    }
}