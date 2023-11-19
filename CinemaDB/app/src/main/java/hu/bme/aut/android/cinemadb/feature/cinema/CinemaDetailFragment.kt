package hu.bme.aut.android.cinemadb.feature.cinema

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.SpannableString
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.material.button.MaterialButton
import hu.bme.aut.android.cinemadb.DatePickerDialogFragment
import hu.bme.aut.android.cinemadb.MainActivity
import hu.bme.aut.android.cinemadb.MapsFragment
import hu.bme.aut.android.cinemadb.R
import hu.bme.aut.android.cinemadb.databinding.FragmentCinemaDetailBinding
import hu.bme.aut.android.cinemadb.model.cinema.CinemaResponse.Body.Cinema
import hu.bme.aut.android.cinemadb.model.cinema.CinemaViewModel
import hu.bme.aut.android.cinemadb.model.filmEvent.FilmEventResponse
import hu.bme.aut.android.cinemadb.model.filmEvent.FilmEventViewModel
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar


class CinemaDetailFragment : Fragment() {
    private lateinit var binding: FragmentCinemaDetailBinding

    private lateinit var cinema: Cinema
    private lateinit var cinemaViewModel: CinemaViewModel
    private var filmEventResponse: FilmEventResponse? = null
    private lateinit var filmEventViewModel: FilmEventViewModel

    private var chosenDate = LocalDate.now()
    private lateinit var datePickerDialog: DatePickerDialogFragment

    @SuppressLint("ClickableViewAccessibility")
    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCinemaDetailBinding.inflate(inflater, container, false)

        cinemaViewModel = viewModels<CinemaViewModel>().value
        filmEventViewModel = viewModels<FilmEventViewModel>().value

//        binding.cinemaDateText.setText(DateTimeFormatter.ofPattern("MM-dd").format(chosenDate))
        binding.cinemaDateText.setOnClickListener {
            findNavController().navigate(R.id.action_cinemaDetailFragment_to_datePickerDialogFragment)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        findNavController()
            .currentBackStackEntry
            ?.savedStateHandle
            ?.getLiveData<String>("cinema")
            ?.observe(viewLifecycleOwner) { cinemaId ->
                cinemaViewModel.cinemaResponse.observe(viewLifecycleOwner) {
                    cinema = it.first?.body?.cinemas?.firstOrNull { cinema -> cinema.id == cinemaId }!!
                    filmEventViewModel.loadResponseForCinema(cinemaId)
                    loadCinema()
                    createEventButtons(cinemaId)
                }
            }

        findNavController()
            .currentBackStackEntry
            ?.savedStateHandle
            ?.getLiveData<DatePickerDialogFragment.DatePickerResult>("date")
            ?.observe(viewLifecycleOwner) { datePickerResult ->
                onDateSet(datePickerResult.year, datePickerResult.month, datePickerResult.day)
            }
    }

    private fun onDateSet(year: Int, month: Int, dayOfMonth: Int) {
        chosenDate = LocalDate.of(year, month, dayOfMonth)
        binding.cinemaDateText.setText(DateTimeFormatter.ofPattern("MM-dd").format(chosenDate))
        filmEventViewModel.loadResponseForCinema(cinema.id, chosenDate)
    }

    private fun loadCinema() {
        binding.cinemaName.text = cinema.displayName
        binding.cinemaAddressButton.setOnClickListener {
            findNavController().navigate(R.id.action_cinemaDetailFragment_to_mapsFragment)
            findNavController().currentBackStackEntry?.savedStateHandle?.set(
                MapsFragment.cinemaId,
                cinema.id
            )
            (activity as MainActivity).navigationView.setCheckedItem(R.id.nav_map_fragment)
            (activity as MainActivity).title = cinema.displayName
        }

        binding.cinemaLinkButton.setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(cinema.link))
            startActivity(browserIntent)
        }

        Glide.with(this)
            .load(cinema.imageUrl)
            .into(binding.cinemaImage)
    }

    @SuppressLint("SetTextI18n")
    private fun createEventButtons(cinemaId: String) {
        filmEventViewModel.filmEventResponse.observe(viewLifecycleOwner) {
            binding.cinemaEventsLinearLayout.removeAllViews()

            val linearLayouts = mutableMapOf<String, LinearLayout>()

            filmEventResponse = it[cinemaId]?.first
            filmEventResponse?.body?.events?.forEach { event ->
                val filmLinearLayout: LinearLayout?
                val buttonLinearLayout: LinearLayout?
                val films = filmEventResponse?.body?.films
                if (linearLayouts.containsKey(event.filmId)) {
                    filmLinearLayout = linearLayouts[event.filmId]
                    buttonLinearLayout = linearLayouts[event.filmId]?.findViewById("film${event.filmId}".hashCode())!!
                } else {
                    filmLinearLayout = LinearLayout(context)

                    filmLinearLayout.id = "film${event.filmId}".hashCode()
                    filmLinearLayout.orientation = LinearLayout.VERTICAL
                    val label = TextView(context)
                    val filmName = films?.firstOrNull { film -> film.id == event.filmId }?.name!!
                    val spannableString = SpannableString(filmName)
                    spannableString.setSpan(android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 0, filmName.length, 0)
                    label.text = spannableString
                    label.textSize = 18f
                    filmLinearLayout.addView(label)
                    (label.layoutParams as ViewGroup.MarginLayoutParams).topMargin = 80

                    buttonLinearLayout = LinearLayout(context)
                    buttonLinearLayout.orientation = LinearLayout.HORIZONTAL
                    filmLinearLayout.addView(buttonLinearLayout)

                    binding.cinemaEventsLinearLayout.addView(filmLinearLayout)
                }

                val filmEventButton = MaterialButton(requireContext())
                val format = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
                val localDateTime = LocalDateTime.parse(event.eventDateTime, format)
                val filmFormat = event.attributeIds.find { attributeId -> attributeId == "3d" || attributeId == "imax" || attributeId == "4dx" }
                filmEventButton.text = DateTimeFormatter.ofPattern("HH:mm").format(localDateTime) + if (filmFormat != null) " ${filmFormat.uppercase()}" else ""
                filmEventButton.setOnClickListener {
                    val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://booking.cinemacity.hu/SalesHU?key=${event.compositeBookingLink.bookingUrl.params.key}&ec=${event.id}&bt=0"))
                    startActivity(browserIntent)
                }

                buttonLinearLayout.addView(filmEventButton)
                linearLayouts[event.filmId] = filmLinearLayout!!
                filmEventButton.layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT
            }

            if (filmEventResponse?.body?.events?.isEmpty() == true) {
                val label = TextView(context)
                label.text = getString(R.string.no_events)
                label.textSize = 18f
                binding.cinemaEventsLinearLayout.addView(label)
                label.gravity = View.TEXT_ALIGNMENT_CENTER
                (label.layoutParams as ViewGroup.MarginLayoutParams).topMargin = 80
            }
        }
    }

    companion object {
        const val cinema = "cinema"
        const val date = "date"
    }
}