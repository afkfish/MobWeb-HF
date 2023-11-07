package hu.bme.aut.android.cinemadb

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import com.google.android.material.navigation.NavigationView
import hu.bme.aut.android.cinemadb.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

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
                val navController = findNavController(R.id.nav_host_fragment)
                when (menuItem.itemId) {
                    R.id.nav_film_fragment -> navController.navigate(R.id.filmListFragment)
                    R.id.nav_cinema_fragment -> navController.navigate(R.id.cinemaListFragment)
                    R.id.nav_map_fragment -> navController.navigate(R.id.mapsFragment)
                }

                menuItem.isChecked = true
                title = menuItem.title
                drawerLayout?.closeDrawers()
            }

            true
        }
    }
}