package hu.bme.aut.android.cinemadb

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import hu.bme.aut.android.cinemadb.databinding.FragmentMapsBinding
import hu.bme.aut.android.cinemadb.feature.cinema.CinemaDataHolder
import hu.bme.aut.android.cinemadb.feature.cinema.CinemaListFragment

class MapsFragment : Fragment() {
    private lateinit var binding: FragmentMapsBinding

    private lateinit var cinemaPositions: List<CinemaPosition>
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

    private val callback = OnMapReadyCallback { googleMap ->
        getCinemaPositions()
        cinemaPositions.forEach {
            googleMap.addMarker(MarkerOptions().position(it.position).title(it.name))
        }
        if (cinemaPositions.isEmpty()) {
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(LatLng(0.0, 0.0)))
        } else {
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(cinemaPositions[0].position))
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMapsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }

    private fun getCinemaPositions() {
        cinemaPositions = cinemaDataHolder?.getCinemaResponse()?.body?.cinemas?.map {
            CinemaPosition(
                it.displayName,
                LatLng(it.latitude, it.longitude)
            )
        } ?: emptyList()
    }

    data class CinemaPosition(val name: String, val position: LatLng)
}