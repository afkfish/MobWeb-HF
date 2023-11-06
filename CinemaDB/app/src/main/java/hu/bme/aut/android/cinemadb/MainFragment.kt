package hu.bme.aut.android.cinemadb

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import hu.bme.aut.android.cinemadb.databinding.FragmentMainBinding


class MainFragment : Fragment() {
    private lateinit var binding: FragmentMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnMaps.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_mapsFragment)
        }

        binding.btnFilms.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_filmListFragment)
        }

        binding.btnCinemas.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_cinemaListFragment)
        }
    }
}