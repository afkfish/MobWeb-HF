package hu.bme.aut.android.cinemadb.feature.cinema

import hu.bme.aut.android.cinemadb.model.cinema.CinemaResponse

interface CinemaDataHolder {
    fun getCinemaResponse(): CinemaResponse?
}