package com.rbs.predictor

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.NavController
import androidx.navigation.ui.setupWithNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = FirebaseAuth.getInstance()

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar) // Set the Toolbar as the app's action bar

        supportActionBar?.setDisplayShowTitleEnabled(false) // default title disabled

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        // Get a reference to your BottomNavigationView
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_nav)

        // Set up BottomNavigationView with NavController
        bottomNavigationView.setupWithNavController(navController)

        // Listen to navigation changes to update the toolbar title
        navController.addOnDestinationChangedListener { _, destination, _ ->
            supportActionBar?.title = destination.label // This will update toolbar title based on fragment label
        }

        val currentUser = auth.currentUser // Authentication Navigation Logic
    }
    //region Top Menu (Overflow Menu) Handling
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Inflate the top_nav_menu.xml to create the overflow menu items
        menuInflater.inflate(R.menu.top_nav_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item clicks from the top overflow menu
        return when (item.itemId) {
            R.id.welcomeFragment -> {
                navController.navigate(R.id.welcomeFragment)
                true
            }
            R.id.predictionFragment -> {
                navController.navigate(R.id.predictionFragment)
                true
            }
            R.id.settingsFragment -> {
                navController.navigate(R.id.settingsFragment)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}