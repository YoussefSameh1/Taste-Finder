package com.example.TasteFinder.favorite

import FavoritesAdapter
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.TasteFinder.R
import com.example.TasteFinder.data.Restaurant
import com.example.TasteFinder.data.User
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class FavoritesFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var emptyView: TextView
    private lateinit var progressBar: ProgressBar

    private lateinit var favoritesAdapter: FavoritesAdapter
    private val favoriteItems = mutableListOf<Restaurant>()
    private lateinit var user : User

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_favorites, container, false)
        val userID = arguments?.getString("userID")
        user = User()
        Firebase.firestore.collection("Users").document(userID!!).get().addOnSuccessListener {
            document->
            user = document.toObject(User::class.java)!!
            Log.d("FavoritesFragment", "User favorites: ${user.favorites}")
        }
        recyclerView = view.findViewById(R.id.recyclerView)
        emptyView = view.findViewById(R.id.emptyView)
        progressBar = view.findViewById(R.id.progressBar)
        favoritesAdapter = FavoritesAdapter(favoriteItems)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = favoritesAdapter

        loadFavorites()

        return view
    }

    private fun loadFavorites() {
        progressBar.visibility = View.VISIBLE
        Firebase.firestore.collection("Restaurants")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents){
                    if(document.id  in user.favorites){

                        val restaurant = document.toObject(Restaurant::class.java)
                        Log.d("FavoritesFragment", "Restaurant: ${restaurant.name}")
                        favoriteItems.add(restaurant.copy(id = document.id))
                    }
                }
                updateUI()
                favoritesAdapter.updateCategories(favoriteItems)
            }
            .addOnFailureListener { exception ->
                Log.w("FirestoreError", "Error getting documents: ", exception)
                progressBar.visibility = View.GONE
                // Handle failure, perhaps by showing an error message or retry option

            }

    }

    private fun updateUI() {
        progressBar.visibility = View.GONE
        if (user.favorites.isEmpty()) {
            emptyView.visibility = View.VISIBLE
            recyclerView.visibility = View.GONE
        } else {
            emptyView.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
        }
    }
}