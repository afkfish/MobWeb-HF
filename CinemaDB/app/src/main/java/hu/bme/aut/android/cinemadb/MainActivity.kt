package hu.bme.aut.android.cinemadb

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.google.android.material.navigation.NavigationView
import hu.bme.aut.android.cinemadb.databinding.ActivityMainBinding
import hu.bme.aut.android.cinemadb.feature.cinema.CinemaDataHolder
import hu.bme.aut.android.cinemadb.feature.cinema.CinemaListFragment
import hu.bme.aut.android.cinemadb.feature.film.FilmDataHolder
import hu.bme.aut.android.cinemadb.feature.film.FilmListFragment
import hu.bme.aut.android.cinemadb.model.cinema.CinemaResponse
import hu.bme.aut.android.cinemadb.model.film.FilmResponse
import hu.bme.aut.android.cinemadb.network.NetworkManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.reflect.KClass


class MainActivity : AppCompatActivity(), FilmDataHolder, CinemaDataHolder {
    private lateinit var binding: ActivityMainBinding
    private var filmResponse: FilmResponse? = null
    private var cinemaResponse: CinemaResponse? = null

    private var drawerLayout: DrawerLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.baseline_menu_24)

        drawerLayout = binding.drawerLayout
        setupDrawerContent(binding.nvView)

        setContentView(binding.root)
//
//        val fragmentManager: FragmentManager = supportFragmentManager
//        fragmentManager.commit {
//            setReorderingAllowed(true)
//            replace<FilmListFragment>(binding.flContent.id)
//        }
//        title = getString(R.string.films)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                drawerLayout?.openDrawer(GravityCompat.START)
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun setupDrawerContent(navigationView: NavigationView) {
        navigationView.setNavigationItemSelectedListener { menuItem ->

            supportFragmentManager.commit {
                setReorderingAllowed(true)
                when (menuItem.itemId) {
                    R.id.nav_film_fragment -> replace<FilmListFragment>(binding.flContent.id)
                    R.id.nav_cinema_fragment -> replace<CinemaListFragment>(binding.flContent.id)
                    R.id.nav_map_fragment -> replace<MapsFragment>(binding.flContent.id)
                    else -> replace<FilmListFragment>(binding.flContent.id)
                }
            }

            // Highlight the selected item has been done by NavigationView
            menuItem.isChecked = true
            // Set action bar title
            title = menuItem.title
            // Close the navigation drawer
            drawerLayout?.closeDrawers()

            true
        }
    }


    override fun onResume() {
        super.onResume()

        loadFilmResponse()
        loadCinemaResponse()
    }

    override fun getFilmResponse(): FilmResponse? {
        return filmResponse
    }

    override fun getCinemaResponse(): CinemaResponse? {
        return cinemaResponse
    }

    private fun loadFilmResponse() {
        val TAG = "FilmListFragment"
        NetworkManager.getFilms()?.enqueue(object : Callback<FilmResponse?> {
            override fun onResponse(
                call: Call<FilmResponse?>,
                response: Response<FilmResponse?>
            ) {
                Log.d(TAG, "onResponse: " + response.code())
                if (response.isSuccessful) {
                    filmResponse = response.body()
                } else {
                    Log.d(TAG, "onResponse: " + response.errorBody())
                }
            }

            override fun onFailure(
                call: Call<FilmResponse?>,
                throwable: Throwable
            ) {
                throwable.printStackTrace()
                Log.d(TAG, "onFailure: " + throwable.message)
            }
        })
    }

    private fun loadCinemaResponse() {
        val TAG = "CinemaListFragment"
        NetworkManager.getCinemas()?.enqueue(object : Callback<CinemaResponse?> {
            override fun onResponse(
                call: Call<CinemaResponse?>,
                response: Response<CinemaResponse?>
            ) {
                Log.d(TAG, "onResponse: " + response.code())
                if (response.isSuccessful) {
                    cinemaResponse = response.body()
                } else {
                    Log.d(TAG, "onResponse: " + response.errorBody())
                }
            }

            override fun onFailure(
                call: Call<CinemaResponse?>,
                throwable: Throwable
            ) {
                throwable.printStackTrace()
                Log.d(TAG, "onFailure: " + throwable.message)
            }
        })
    }
}