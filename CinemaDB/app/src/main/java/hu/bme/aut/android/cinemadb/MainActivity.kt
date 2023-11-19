package hu.bme.aut.android.cinemadb

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import com.google.android.material.navigation.NavigationView
import hu.bme.aut.android.cinemadb.databinding.ActivityMainBinding
import hu.bme.aut.android.cinemadb.model.cinema.CinemaViewModel
import hu.bme.aut.android.cinemadb.model.film.FilmViewModel
import hu.bme.aut.android.cinemadb.model.filmEvent.FilmEventViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    lateinit var filmViewModel: FilmViewModel
    lateinit var cinemaViewModel: CinemaViewModel
    lateinit var filmEventViewModel: FilmEventViewModel
    private var drawerLayout: DrawerLayout? = null

    lateinit var searchItem: MenuItem
    lateinit var navigationView: NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.baseline_menu_24)

        drawerLayout = binding.drawerLayout
        setupDrawerContent(binding.nvView)
        navigationView = binding.nvView

        filmViewModel = viewModels<FilmViewModel>().value
        cinemaViewModel = viewModels<CinemaViewModel>().value
        filmEventViewModel = viewModels<FilmEventViewModel>().value

        filmEventViewModel.loadResponseForCinema("1124")

        title = getString(R.string.films)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        searchItem = menu?.findItem(R.id.search_button)!!
        val searchView = searchItem.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                filterByActiveFragment(newText)
                return false
            }
        })

        searchView.setOnCloseListener {
            filterByActiveFragment("")
            false
        }
        return true
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
                val navController = findNavController(R.id.nav_host_fragment)
                when (menuItem.itemId) {
                    R.id.nav_film_fragment -> navController.navigate(R.id.filmListFragment)
                    R.id.nav_cinema_fragment -> navController.navigate(R.id.cinemaListFragment)
                    R.id.nav_map_fragment -> navController.navigate(R.id.mapsFragment)
                }
                filterByActiveFragment("")
                menuItem.isChecked = true
                title = menuItem.title
                drawerLayout?.closeDrawers()
            }

            true
        }
    }

    private fun filterByActiveFragment(query: String) {
        when (findNavController(R.id.nav_host_fragment).currentDestination?.id) {
            R.id.filmListFragment -> {
                if (!filmViewModel.filter(query)) {
                    Toast.makeText(this, "No match found", Toast.LENGTH_SHORT).show()
                }
            }
            R.id.cinemaListFragment -> {
                if (!cinemaViewModel.filter(query)) {
                    Toast.makeText(this, "No match found", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}