package com.rbs.predictor // Use your actual root package name

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = FirebaseAuth.getInstance()

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        // Check if the user is currently signed in (authentication logic goes here later)
        val currentUser = auth.currentUser

        // --- Authentication Navigation Logic Placeholder ---
        // In a real app with login:
        // If currentUser is null, navigate to a Login/Welcome screen.
        // If currentUser is not null, you might navigate directly to the Prediction screen
        // (or check subscription status first).
        // For now, the nav_graph's start destination (Welcome) will be shown by default.
        // You'll add navigation logic here in a later step.
        // ---------------------------------------------------
    }

    // You might add methods here later to handle Google Sign-In results if using a sign-in button
}