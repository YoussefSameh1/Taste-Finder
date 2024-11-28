package com.example.TasteFinder.home

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import com.example.TasteFinder.BaseActivity
import com.example.TasteFinder.categories.CategoriesFragment
import com.example.TasteFinder.nearBy.NearbyFragment
import com.example.TasteFinder.R
import com.example.TasteFinder.favorite.FavoritesFragment
import com.example.TasteFinder.search.SearchFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeActivity : BaseActivity() {

    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home)
        val user = intent.getStringExtra("userID")
        Log.d("HomeActivityUser", user.toString())

        // Set the default fragment to CategoriesFragment
        if (savedInstanceState == null) {

            val categoryFragment = CategoriesFragment()
            val bundle = Bundle()
            bundle.putString("userID", user)
            categoryFragment.arguments = bundle
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, categoryFragment)
                .commit()
        }

        bottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigationView.setOnItemSelectedListener { item: MenuItem ->
            when (item.itemId) {
                R.id.nav_home -> {
                    val categoryFragment = CategoriesFragment()
                    val bundle = Bundle()
                    bundle.putString("userID", user)
                    categoryFragment.arguments = bundle
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainer, categoryFragment)
                        .addToBackStack(null)
                        .commit()
                }
                R.id.nav_favorites -> {
                    val favoritesFragment = FavoritesFragment()
                    val bundle = Bundle()
                    bundle.putString("userID", user)
                    favoritesFragment.arguments = bundle
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainer, favoritesFragment)
                        .addToBackStack(null)
                        .commit()
                }
                R.id.nav_search -> {
                    val searchFragment = SearchFragment()
                    val bundle = Bundle()
                    bundle.putString("userID", user)
                    searchFragment.arguments = bundle
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainer, searchFragment)
                        .addToBackStack(null)
                        .commit()
                }
                R.id.nav_location -> {
                    val nearbyFragment = NearbyFragment()
                    val bundle = Bundle()
                    bundle.putString("userID", user)
                    nearbyFragment.arguments = bundle
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainer, nearbyFragment)
                        .addToBackStack(null)
                        .commit()
                }
            }
            true
        }
    }

}

