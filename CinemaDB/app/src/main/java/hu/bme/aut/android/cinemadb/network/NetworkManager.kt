package hu.bme.aut.android.cinemadb.network

import hu.bme.aut.android.cinemadb.model.cinema.CinemaResponse
import hu.bme.aut.android.cinemadb.model.film.FilmResponse
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.LocalDate
import java.time.format.DateTimeFormatter

object NetworkManager {
    private val retrofit: Retrofit
    private val cinemaCityApi: CinemaCityApi
    private const val SERVICE_URL = "https://www.cinemacity.hu/"
    private lateinit var date: String

    init {
        updateDate()
        retrofit = Retrofit.Builder()
            .baseUrl(SERVICE_URL)
            .client(OkHttpClient.Builder().build())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        cinemaCityApi = retrofit.create(CinemaCityApi::class.java)
    }

    fun getCinemas(): Call<CinemaResponse?>? {
        updateDate()
        return cinemaCityApi.getCinemas(date)
    }

    fun getFilms(): Call<FilmResponse?>? {
        updateDate()
        return cinemaCityApi.getFilms(date)
    }

    private fun updateDate() {
        date = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDate.now().plusYears(1))
    }
}