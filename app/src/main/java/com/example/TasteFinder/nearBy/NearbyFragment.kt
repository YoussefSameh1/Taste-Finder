package com.example.TasteFinder.nearBy

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.TasteFinder.R
import com.example.TasteFinder.data.OnRestaurantClickListener
import com.example.TasteFinder.data.Restaurant
import com.example.TasteFinder.restaurantDetails.RestaurantDetailsActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class NearbyFragment : Fragment(), OnRestaurantClickListener {
    private lateinit var restaurantRef: CollectionReference
    private val restaurantList = mutableListOf<Restaurant>()
    private val distances = mutableListOf<Float>()
    private var remainingCalls = 0
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_nearby, container, false)
        val user = arguments?.getString("userID")
        restaurantRef = Firebase.firestore.collection("Restaurants")
        recyclerView = view.findViewById(R.id.nearByRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        fetchRestaurants()


        return view
    }

    private fun fetchRestaurants() {
        restaurantRef.get().addOnSuccessListener { documents ->
            remainingCalls = documents.size() // Set the count of remaining calls
            for (document in documents) {
                val restaurant = document.toObject(Restaurant::class.java)
                restaurantList.add(restaurant.copy(id = document.id))

                calculateDistanceFromUrlToCurrentLocation(
                    requireContext(),
                    restaurant.location
                ) { distance ->
                    distances.add(distance ?: 0.0f)
                    // Decrement the remaining calls counter
                    remainingCalls--

                    if (remainingCalls == 0) {
                        updateRecyclerView()
                    }
                }
            }
        }
    }

    private fun updateRecyclerView() {
        val sortedPair = restaurantList.zip(distances).sortedBy { it.second }

        recyclerView.adapter = NearByAdapter(sortedPair, this)
    }


    private fun extractCoordinatesFromUrl(url: String): Pair<Double, Double>? {
        val regex = "@([-\\d.]+),([-\\d.]+)".toRegex()
        val matchResult = regex.find(url)

        return matchResult?.let {
            val (latitude, longitude) = it.destructured
            Log.d("latitude", latitude)
            Log.d("longitude", longitude)
            Pair(latitude.toDouble(), longitude.toDouble())
        }
    }

    private fun calculateDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Float {
        val result = FloatArray(1)
        Location.distanceBetween(lat1, lon1, lat2, lon2, result)
        return result[0] // Distance in meters
    }
    private fun calculateDistanceFromUrlToCurrentLocation(context: Context, url: String, callback: (Float?) -> Unit) {
        val destinationCoordinates = extractCoordinatesFromUrl(url)

        if (destinationCoordinates == null) {
            callback(null)
            return
        }

        getCurrentLocation(context) { currentLocation ->
            if (currentLocation != null) {
                val distance = calculateDistance(
                    currentLocation.latitude,
                    currentLocation.longitude,
                    destinationCoordinates.first,
                    destinationCoordinates.second
                )
                callback(distance)
            } else {
                callback(null)
            }
        }

    }

    private fun getCurrentLocation(context: Context, callback: (Location?) -> Unit) {
        val fusedLocationClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)

        if (
            ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }else{
            fusedLocationClient.lastLocation.addOnCompleteListener { task: Task<Location> ->
                if (task.isSuccessful && task.result != null) {
                    callback(task.result)
                } else {
                    callback(null)
                }
            }
        }

    }

    override fun onRestaurantClick(restaurant: Restaurant) {
        val intent = Intent(requireContext(), RestaurantDetailsActivity::class.java)
        intent.putExtra("restaurant", restaurant)
        startActivity(intent)
    }


}

