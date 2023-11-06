package hu.bme.aut.android.cinemadb.feature.cinema

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import hu.bme.aut.android.cinemadb.R
import hu.bme.aut.android.cinemadb.databinding.FragmentCinemaListBinding
import hu.bme.aut.android.cinemadb.model.cinema.CinemaResponse

/**
 * A fragment representing a list of Items.
 */
class CinemaListFragment : Fragment(), MyCinemaListRecyclerViewAdapter.OnCinemaSelectedListener {
    private lateinit var binding: FragmentCinemaListBinding

    private var cinemaResponse: CinemaResponse? = null
    private var cinemaDataHolder: CinemaDataHolder? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        cinemaDataHolder = if (activity is CinemaDataHolder) {
            activity as CinemaDataHolder?
        } else {
            throw RuntimeException(
                "Activity must implement CinemaDataHolder interface!"
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCinemaListBinding.inflate(inflater, container, false)
        binding.list.layoutManager = LinearLayoutManager(context)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (cinemaDataHolder?.getCinemaResponse() != null) {
            cinemaResponse = cinemaDataHolder?.getCinemaResponse()
            displayCinemaResponse()
        }
    }

    override fun onCinemaSelected() {
        findNavController().navigate(R.id.action_cinemaListFragment_to_cinemaDetailFragment)
    }

    private fun displayCinemaResponse() {
        binding.list.adapter = MyCinemaListRecyclerViewAdapter(cinemaResponse!!, this)
    }
}