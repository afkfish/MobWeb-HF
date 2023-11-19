package hu.bme.aut.android.cinemadb.model.film

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import hu.bme.aut.android.cinemadb.network.NetworkManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Locale
import hu.bme.aut.android.cinemadb.model.film.FilmResponse.Body.Film

class FilmViewModel : ViewModel() {
    val filmResponse: MutableLiveData<Pair<FilmResponse?, Error?>> = MutableLiveData()
    val filteredList: MutableLiveData<List<Film>> = MutableLiveData()

    init {
        loadFilmResponse {
            filmResponse.value = it
            filteredList.value = it.first?.body?.films as MutableList<Film>?
        }
    }

    private fun loadFilmResponse(completion: (Pair<FilmResponse?, Error?>) -> Unit) {
        val TAG = "FilmListFragment"
        NetworkManager.getFilms()?.enqueue(object : Callback<FilmResponse?> {
            override fun onResponse(
                call: Call<FilmResponse?>,
                response: Response<FilmResponse?>
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
                call: Call<FilmResponse?>,
                throwable: Throwable
            ) {
                throwable.printStackTrace()
                Log.d(TAG, "onFailure: " + throwable.message)
                completion(Pair(null, Error(throwable.message)))
            }
        })
    }

    fun filter(query: String): Boolean {
        val tempList = mutableListOf<Film>()
        for (film in filmResponse.value?.first?.body?.films!!) {
            if (film.name.lowercase(Locale.ROOT).contains(query.lowercase(Locale.ROOT))) {
                tempList.add(film)
            }
        }
        filteredList.postValue(tempList)
        return tempList.isNotEmpty()
    }
}