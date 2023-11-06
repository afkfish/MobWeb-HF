package hu.bme.aut.android.cinemadb.feature.cinema

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import hu.bme.aut.android.cinemadb.R
import hu.bme.aut.android.cinemadb.databinding.FragmentCinemaListBinding
import hu.bme.aut.android.cinemadb.model.cinema.CinemaResponse
import hu.bme.aut.android.cinemadb.model.cinema.CinemaViewModel

/**
 * A fragment representing a list of Items.
 */
class CinemaListFragment : Fragment(), MyCinemaListRecyclerViewAdapter.OnCinemaSelectedListener {
    private lateinit var binding: FragmentCinemaListBinding
    private lateinit var adapter: MyCinemaListRecyclerViewAdapter
    private lateinit var cinemaDataHolder: CinemaViewModel
    private var cinemaResponse: CinemaResponse? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        adapter = MyCinemaListRecyclerViewAdapter(this)

        cinemaDataHolder = viewModels<CinemaViewModel>().value
        cinemaDataHolder.cinemaResponse.observe(this) {
            cinemaResponse = it.first
            displayCinemaResponse()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCinemaListBinding.inflate(inflater, container, false)
        binding.list.layoutManager = LinearLayoutManager(context)
        binding.list.adapter = adapter
        return binding.root
    }

    override fun onCinemaSelected() {
        findNavController().navigate(R.id.action_cinemaListFragment_to_cinemaDetailFragment)
    }

    private fun displayCinemaResponse() {
        adapter.updateCinemas(cinemaResponse!!.body.cinemas)
    }
}