package hu.bme.aut.android.cinemadb.network

import hu.bme.aut.android.cinemadb.model.cinema.CinemaResponse
import hu.bme.aut.android.cinemadb.model.film.FilmResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface CinemaCityApi {
    // https://www.cinemacity.hu/hu/data-api-service/v1/quickbook/10102/films/until/2024-10-03
    @GET("/hu/data-api-service/v1/quickbook/10102/films/until/{date}")
    fun getFilms(
        @Path("date") date: String
    ): Call<FilmResponse?>?

    // https://www.cinemacity.hu/hu/data-api-service/v1/quickbook/10102/cinemas/with-event/until/2024-10-03
    @GET("/hu/data-api-service/v1/quickbook/10102/cinemas/with-event/until/{date}")
    fun getCinemas(
        @Path("date") date: String
    ): Call<CinemaResponse?>?
}