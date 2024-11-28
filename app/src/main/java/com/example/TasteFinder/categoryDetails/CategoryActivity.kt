package com.example.TasteFinder.categoryDetails

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.widget.ProgressBar
import android.widget.TextView
import com.example.TasteFinder.BaseActivity
import com.example.TasteFinder.R
import com.example.TasteFinder.data.OnRestaurantClickListener
import com.example.TasteFinder.data.Restaurant
import com.example.TasteFinder.restaurantDetails.RestaurantDetailsActivity
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class CategoryActivity : BaseActivity() , OnRestaurantClickListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var emptyView: TextView
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category)
        recyclerView = findViewById(R.id.recyclerView)
        emptyView = findViewById(R.id.emptyView)
        progressBar = findViewById(R.id.progressBar)
        val categoryAdapter = CategoryDetailsAdapter(listOf(), this)
        val categoryID = intent.getStringExtra("categoryID")
 // Start with empty list
        recyclerView.adapter = categoryAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        Firebase.firestore.collection("Restaurants").whereArrayContains("Categories", categoryID ?: "").get().addOnSuccessListener {
            documents ->
            val restaurants = mutableListOf<Restaurant>()
            for (document in documents) {
                val restaurant = document.toObject(Restaurant::class.java)
                restaurants.add(restaurant.copy(id = document.id))
                }
            recyclerView.adapter = CategoryDetailsAdapter(restaurants, this)
            progressBar.visibility =View.GONE
        }
    }

    override fun onRestaurantClick(restaurant: Restaurant) {
        val intent = Intent(this, RestaurantDetailsActivity::class.java)
        intent.putExtra("restaurant", restaurant)
        startActivity(intent)
    }
}
