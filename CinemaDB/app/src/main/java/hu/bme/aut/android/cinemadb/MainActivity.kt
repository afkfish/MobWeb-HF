package hu.bme.aut.android.cinemadb

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.add
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.google.android.material.navigation.NavigationView
import hu.bme.aut.android.cinemadb.databinding.ActivityMainBinding
import hu.bme.aut.android.cinemadb.feature.cinema.CinemaListFragment
import hu.bme.aut.android.cinemadb.feature.film.FilmListFragment
import hu.bme.aut.android.cinemadb.model.cinema.CinemaResponse
import hu.bme.aut.android.cinemadb.model.cinema.CinemaViewModel
import hu.bme.aut.android.cinemadb.model.film.FilmResponse
import hu.bme.aut.android.cinemadb.model.film.FilmViewModel


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val filmViewModel: FilmViewModel
        get() = FilmViewModel()

    private val cinemaViewModel: CinemaViewModel
        get() = CinemaViewModel()

    private var filmResponse: FilmResponse? = null
    private var cinemaResponse: CinemaResponse? = null

    private var drawerLayout: DrawerLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.baseline_menu_24)

        drawerLayout = binding.drawerLayout
        setupDrawerContent(binding.nvView)

        initFilmRecyclerView()
    }

    private fun initFilmRecyclerView() {
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            add<FilmListFragment>(binding.flContent.id)
        }
        title = getString(R.string.films)
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

            if (!menuItem.isChecked) {
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
            }
            // Close the navigation drawer
            drawerLayout?.closeDrawers()

            true
        }
    }


    override fun onResume() {
        super.onResume()

        filmViewModel.filmResponse.observe(this) {
            filmResponse = it.first
        }

        cinemaViewModel.cinemaResponse.observe(this) {
            cinemaResponse = it.first
        }
    }
}