package hu.bme.aut.android.cinemadb.feature.film

import hu.bme.aut.android.cinemadb.model.film.FilmResponse

interface FilmDataHolder {
    fun getFilmResponse(): FilmResponse?
}