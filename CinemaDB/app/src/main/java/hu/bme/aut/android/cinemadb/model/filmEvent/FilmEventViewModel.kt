package hu.bme.aut.android.cinemadb.model.filmEvent

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import hu.bme.aut.android.cinemadb.network.NetworkManager
import kotlinx.coroutines.awaitAll
import java.time.Instant
import java.time.LocalDate

class FilmEventViewModel : ViewModel() {
    val filmEventResponse: MutableLiveData<Map<String, Pair<FilmEventResponse?, Error?>>> = MutableLiveData(mapOf())

    init {
        loadFilmEventResponse("1144") {
            filmEventResponse.value?.plus(Pair("1144", it))
        }
    }

    // fontos link https://booking.cinemacity.hu/SalesHU?key=HUMammutP_RES&ec=54603&bt=0

    private fun loadFilmEventResponse(cinemaId: String, date: LocalDate = LocalDate.now(), completion: (Pair<FilmEventResponse?, Error?>) -> Unit) {
        val TAG = "FilmEventListFragment"
        NetworkManager.getFilmEventsByCinema(cinemaId, date)?.enqueue(object : Callback<FilmEventResponse?> {
            override fun onResponse(
                call: Call<FilmEventResponse?>,
                response: Response<FilmEventResponse?>
            ) {
                Log.d(TAG, "onResponse: " + response.code())
                if (response.isSuccessful) {
                    completion(Pair(response.body(), null))
                } else {
                    Log.d(TAG, "onResponse: " + response.errorBody())
                    completion(Pair(null, Error(response.errorBody().toString())))
                }
            }

            override fun onFailure(
                call: Call<FilmEventResponse?>,
                throwable: Throwable
            ) {
                throwable.printStackTrace()
                Log.d(TAG, "onFailure: " + throwable.message)
                completion(Pair(null, Error(throwable.message)))
            }
        })
    }

    fun loadResponseForCinema(cinemaId: String, date: LocalDate = LocalDate.now()) {
        loadFilmEventResponse(cinemaId, date) {
            val map = filmEventResponse.value?.toMutableMap()!!
            map[cinemaId] = it
            filmEventResponse.postValue(map)
        }
    }
}