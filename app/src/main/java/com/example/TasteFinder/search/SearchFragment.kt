package com.example.TasteFinder.search

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.TasteFinder.R
import com.example.TasteFinder.data.OnRestaurantClickListener
import com.example.TasteFinder.data.Restaurant
import com.example.TasteFinder.restaurantDetails.RestaurantDetailsActivity
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore

class SearchFragment : Fragment(), OnRestaurantClickListener {

    private lateinit var searchView: SearchView
    private lateinit var adapter:SearchAdapter
    private lateinit var restaurantRef: CollectionReference
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_search, container, false)
        val user = arguments?.getString("userID")

        searchView = view.findViewById<SearchView>(R.id.searchView2)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        restaurantRef = FirebaseFirestore.getInstance().collection("Restaurants")

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = SearchAdapter(listOf(), this) // Initialize your adapter with an empty list or your data
        recyclerView.adapter = adapter

        searchView.setOnClickListener {
            searchView.isIconified = false // Expand the search view
            searchView.requestFocus() // Request focus to start typing
            // Optionally, you can programmatically show the keyboard
        }
        setupSearchView()
//      TODO add search logic

        return view
    }

    private fun setupSearchView() {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                restaurantRef.orderBy("Name").startAt(query).endAt(query + "\uf8ff").get().addOnSuccessListener {
                    documents ->
                    val restaurants = mutableListOf<Restaurant>()
                    for (document in documents) {
                        val restaurant = document.toObject(Restaurant::class.java)
                        restaurants.add(restaurant.copy(id = document.id))
                    }
                    adapter.updateList(restaurants)

                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }

        })

    }

    override fun onRestaurantClick(restaurant: Restaurant) {
        val intent = Intent(requireContext(), RestaurantDetailsActivity::class.java)
        intent.putExtra("restaurant", restaurant)
        startActivity(intent)
    }

}