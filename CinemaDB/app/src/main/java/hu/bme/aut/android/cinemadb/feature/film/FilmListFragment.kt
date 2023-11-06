package hu.bme.aut.android.cinemadb.feature.film

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import hu.bme.aut.android.cinemadb.R
import hu.bme.aut.android.cinemadb.databinding.FragmentFilmListBinding
import hu.bme.aut.android.cinemadb.model.film.FilmResponse

/**
 * A fragment representing a list of Items.
 */
class FilmListFragment : Fragment(), MyFilmListRecyclerViewAdapter.OnFilmSelectedListener {
    private lateinit var binding: FragmentFilmListBinding

    private var filmResponse: FilmResponse? = null
    private var filmDataHolder: FilmDataHolder? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        filmDataHolder = if (activity is FilmDataHolder) {
            activity as FilmDataHolder?
        } else {
            throw RuntimeException(
                "Activity must implement FilmDataHolder interface!"
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFilmListBinding.inflate(inflater, container, false)
        binding.list.layoutManager = LinearLayoutManager(context)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (filmDataHolder?.getFilmResponse() != null) {
            filmResponse = filmDataHolder?.getFilmResponse()
            displayFilmResponse()
        }
    }

    override fun onFilmSelected() {
        findNavController().navigate(R.id.action_filmFragment_to_filmDetailFragment)
    }

    private fun displayFilmResponse() {
        binding.list.adapter = MyFilmListRecyclerViewAdapter(filmResponse!!, this)
    }
}