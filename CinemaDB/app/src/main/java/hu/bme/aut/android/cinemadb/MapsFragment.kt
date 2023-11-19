package hu.bme.aut.android.cinemadb

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import hu.bme.aut.android.cinemadb.databinding.FragmentMapsBinding
import hu.bme.aut.android.cinemadb.model.cinema.CinemaResponse.Body.Cinema
import hu.bme.aut.android.cinemadb.model.cinema.CinemaResponse
import hu.bme.aut.android.cinemadb.model.cinema.CinemaViewModel
import java.util.Timer
import kotlin.concurrent.schedule


class MapsFragment : Fragment() {
    private lateinit var binding: FragmentMapsBinding

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    private lateinit var cinemaPositions: List<CinemaPosition>
    private lateinit var cinemaViewModel: CinemaViewModel
    private var cinemaResponse: CinemaResponse? = null

    private var cinema: Cinema? = null

    private val DEFAULT_ZOOM = 8f
    private var lastKnownLocation : Location? = null

    private val locationPermissionGranted
        get() = ActivityCompat.checkSelfPermission(
            requireContext(),
            android.Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

    private lateinit var map : GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        if (!locationPermissionGranted) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION),
                1
            )
        }

        cinemaViewModel = viewModels<CinemaViewModel>().value
        cinemaViewModel.cinemaResponse.observe(this) {
            cinemaResponse = it.first
            getCinemaPositions()
        }
    }

    @SuppressLint("MissingPermission")
    private val callback = OnMapReadyCallback { googleMap ->
        map = googleMap
        map.isMyLocationEnabled = locationPermissionGranted

        cinemaPositions.forEach {
            googleMap.addMarker(MarkerOptions().position(it.position).title(it.name))
        }

        googleMap.setOnMarkerClickListener {
            (activity as MainActivity).title = it.title

            // Ennel rosszabb hacket nem tudok XD
            Timer().schedule(5) {
                (activity as MainActivity).runOnUiThread {
                    goTo(it.position, 15f)
                }
            }
            false
        }

        googleMap.setOnMapClickListener {
            (activity as MainActivity).title = getString(R.string.maps)
        }

        if (cinemaPositions.isEmpty()) {
            goTo(LatLng(0.0, 0.0))
        } else if (cinema != null) {
            goTo(LatLng(cinema!!.latitude, cinema!!.longitude), 15f)
        } else {
            getDeviceLocation()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMapsBinding.inflate(inflater, container, false)

        findNavController()
            .currentBackStackEntry
            ?.savedStateHandle
            ?.getLiveData<String>("cinemaId")
            ?.observe(viewLifecycleOwner) {id ->
                cinemaViewModel.cinemaResponse.observe(viewLifecycleOwner) {
                    cinema = it.first?.body?.cinemas?.firstOrNull { cinema -> cinema.id == id }
                }
            }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }

    private fun getCinemaPositions() {
        cinemaPositions = cinemaResponse?.body?.cinemas?.map {
            CinemaPosition(
                it.displayName,
                LatLng(it.latitude, it.longitude)
            )
        } ?: emptyList()
    }

    data class CinemaPosition(val name: String, val position: LatLng)

    companion object {
        const val cinemaId = "cinemaId"
    }

    private fun goTo(position: LatLng, zoom: Float = DEFAULT_ZOOM) {
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(position, zoom))
    }

    @SuppressLint("MissingPermission")
    private fun getDeviceLocation() {
        try {
            if (locationPermissionGranted) {
                val locationResult = fusedLocationProviderClient.lastLocation
                locationResult.addOnCompleteListener(requireActivity()) { task ->
                    if (task.isSuccessful) {
                        lastKnownLocation = task.result
                        if (lastKnownLocation != null) {
                            goTo(LatLng(lastKnownLocation!!.latitude, lastKnownLocation!!.longitude))
                        } else if (cinema != null) {
                            val alle = cinemaResponse?.body?.cinemas?.firstOrNull { cinema -> cinema.displayName.lowercase().contains("alle") }
                            val allePosition = LatLng(alle?.latitude ?: 0.0, alle?.longitude ?: 0.0)
                            goTo(allePosition)
                        } else {
                            goTo(cinemaPositions.first().position)
                        }
                    }
                }
            }
        } catch (e: SecurityException) {
            Toast.makeText(requireContext(), e.message, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onResume() {
        super.onResume()
        if (locationPermissionGranted && cinema != null) {
            getDeviceLocation()
        }
    }
}