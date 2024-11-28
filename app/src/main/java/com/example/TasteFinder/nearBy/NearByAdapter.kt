package com.example.TasteFinder.nearBy

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.TasteFinder.R
import com.example.TasteFinder.data.OnRestaurantClickListener
import com.example.TasteFinder.data.Restaurant
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.Task
import com.squareup.picasso.Picasso

class NearByAdapter(private val items: List<Pair<Restaurant, Float>>, private val listener: OnRestaurantClickListener) : RecyclerView.Adapter<NearByAdapter.ViewHolder>() {
    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val name: TextView = itemView.findViewById(R.id.nearby_name)
        val distance: TextView = itemView.findViewById(R.id.nearby_distance)
        val image: ImageView = itemView.findViewById(R.id.nearby_image)

        fun bind(item: Pair<Restaurant, Float>, listener: OnRestaurantClickListener) {
            itemView.setOnClickListener {
                listener.onRestaurantClick(item.first)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.nearby_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.name.text = items[position].first.name
        holder.distance.text = items[position].second.toString() + " m"
        Picasso.get().load(items[position].first.imageURL).into(holder.image)

        holder.bind(items[position], listener)
    }


}