package hu.bme.aut.android.cinemadb.model.cinema

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import hu.bme.aut.android.cinemadb.network.NetworkManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Locale
import hu.bme.aut.android.cinemadb.model.cinema.CinemaResponse.Body.Cinema

class CinemaViewModel : ViewModel() {
    val cinemaResponse: MutableLiveData<Pair<CinemaResponse?, Error?>> = MutableLiveData()
    val filteredList: MutableLiveData<List<Cinema>> = MutableLiveData()

    init {
        loadCinemaResponse {
            cinemaResponse.value = it
            filteredList.value = it.first?.body?.cinemas as MutableList<Cinema>?
        }
    }

    private fun loadCinemaResponse(completion: (Pair<CinemaResponse?, Error?>) -> Unit) {
        val TAG = "CinemaListFragment"
        NetworkManager.getCinemas()?.enqueue(object : Callback<CinemaResponse?> {
            override fun onResponse(
                call: Call<CinemaResponse?>,
                response: Response<CinemaResponse?>
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
                call: Call<CinemaResponse?>,
                throwable: Throwable
            ) {
                throwable.printStackTrace()
                Log.d(TAG, "onFailure: " + throwable.message)
                completion(Pair(null, Error(throwable.message)))
            }
        })
    }

    fun filter(query: String): Boolean {
        val tempList = mutableListOf<Cinema>()
        for (cinema in cinemaResponse.value?.first?.body?.cinemas!!) {
            if (cinema.displayName.lowercase(Locale.ROOT).contains(query.lowercase(Locale.ROOT))) {
                tempList.add(cinema)
            }
        }
        filteredList.postValue(tempList)
        return tempList.isNotEmpty()
    }
}