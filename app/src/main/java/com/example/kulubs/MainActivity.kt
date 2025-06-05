package com.example.kulubs

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.kulubs.ui.activity.LoginActivity
import com.example.kulubs.ui.fragment.FavoriteFragment
import com.example.kulubs.ui.fragment.ProfileFragment
import com.example.kulubs.ui.fragment.ReviewFragment
import com.example.kulubs.ui.fragment.SearchFragment
import com.google.firebase.auth.FirebaseAuth
import androidx.appcompat.widget.AppCompatImageView

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private var currentFragment: Fragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Check if user is authenticated (fallback)
        if (auth.currentUser == null) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }

        // Setup window insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Setup bottom navigation
        setupBottomNavigation()

        // Load default fragment (SearchFragment)
        if (savedInstanceState == null) {
            replaceFragment(SearchFragment())
            updateNavIconTint(R.id.nav_home)
        }
    }

    private fun setupBottomNavigation() {
        val navHome = findViewById<LinearLayout>(R.id.nav_home)
        val navMenu = findViewById<LinearLayout>(R.id.nav_menu)
        val navFavorites = findViewById<LinearLayout>(R.id.nav_favorites)
        val navProfile = findViewById<LinearLayout>(R.id.nav_profile)

        navHome.setOnClickListener {
            if (currentFragment !is SearchFragment) {
                replaceFragment(SearchFragment())
                updateNavIconTint(R.id.nav_home)
            }
        }

        navMenu.setOnClickListener {
            if (currentFragment !is ReviewFragment) {
                replaceFragment(ReviewFragment())
                updateNavIconTint(R.id.nav_menu)
            }
        }

        navFavorites.setOnClickListener {
            if (currentFragment !is FavoriteFragment) {
                replaceFragment(FavoriteFragment())
                updateNavIconTint(R.id.nav_favorites)
            }
        }

        navProfile.setOnClickListener {
            if (currentFragment !is ProfileFragment) {
                replaceFragment(ProfileFragment())
                updateNavIconTint(R.id.nav_profile)
            }
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        currentFragment = fragment
        supportFragmentManager.beginTransaction()
            .replace(R.id.content_container, fragment)
            .commit()
    }

    private fun updateNavIconTint(selectedNavId: Int) {
        val navIds = listOf(R.id.nav_home, R.id.nav_menu, R.id.nav_favorites, R.id.nav_profile)
        val iconIds = listOf(R.id.nav_home_icon, R.id.nav_menu_icon, R.id.nav_favorites_icon, R.id.nav_profile_icon)

        navIds.forEachIndexed { index, navId ->
            val icon = findViewById<AppCompatImageView>(iconIds[index])
            val tintColor = if (navId == selectedNavId) {
                ContextCompat.getColor(this, R.color.nav_selected)
            } else {
                ContextCompat.getColor(this, R.color.nav_unselected)
            }
            icon.setColorFilter(tintColor)
        }
    }
}