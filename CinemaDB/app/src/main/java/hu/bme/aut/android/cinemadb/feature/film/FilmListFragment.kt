package hu.bme.aut.android.cinemadb.feature.film

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import hu.bme.aut.android.cinemadb.R
import hu.bme.aut.android.cinemadb.databinding.FragmentFilmListBinding
import hu.bme.aut.android.cinemadb.model.film.FilmResponse
import hu.bme.aut.android.cinemadb.model.film.FilmViewModel

/**
 * A fragment representing a list of Items.
 */
class FilmListFragment : Fragment(), MyFilmListRecyclerViewAdapter.OnFilmSelectedListener {
    private lateinit var binding: FragmentFilmListBinding
    private lateinit var adapter: MyFilmListRecyclerViewAdapter
    private lateinit var filmViewModel: FilmViewModel
    private var filmResponse: FilmResponse? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        adapter = MyFilmListRecyclerViewAdapter(this)

        filmViewModel = viewModels<FilmViewModel>().value
        filmViewModel.filmResponse.observe(this) {
            filmResponse = it.first
            displayFilmResponse()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFilmListBinding.inflate(inflater, container, false)
        binding.list.layoutManager = LinearLayoutManager(context)
        binding.list.adapter = adapter
        return binding.root
    }

    override fun onFilmSelected() {
        findNavController().navigate(R.id.action_filmFragment_to_filmDetailFragment)
    }

    private fun displayFilmResponse() {
        adapter.updateFilms(filmResponse!!.body.films)
    }
}