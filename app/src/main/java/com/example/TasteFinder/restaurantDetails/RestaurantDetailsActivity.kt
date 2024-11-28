package com.example.TasteFinder.restaurantDetails

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.TasteFinder.R
import com.example.TasteFinder.data.Restaurant
import com.squareup.picasso.Picasso

class RestaurantDetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_restaurant_details)
        val restaurant = intent.getSerializableExtra("restaurant") as Restaurant
        val restaurantName = findViewById<TextView>(R.id.restaurantName)
        restaurantName.text = restaurant.name
        val restaurantImage = findViewById<ImageView>(R.id.restaurantImage)
        Picasso.get().load(restaurant.imageURL).into(restaurantImage)


        val recyclerView = findViewById<RecyclerView>(R.id.menuRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val menuAdapter = MenuAdapter(restaurant.menu)
        recyclerView.adapter = menuAdapter
        val locationButton = findViewById<Button>(R.id.btn_location)
        locationButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(restaurant.location)
            startActivity(intent)
        }
        val facebookButton = findViewById<Button>(R.id.btn_facebook)
        facebookButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(restaurant.facebookLink)
            startActivity(intent)
        }
        val callButton = findViewById<Button>(R.id.btn_call)
        callButton.setOnClickListener{
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:${restaurant.phoneNumbers[0]}")
            startActivity(intent)
        }

//        TODO add Restaurant Details Here
    }
}