package com.example.TasteFinder.categories

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.TasteFinder.R
import com.example.TasteFinder.data.OnRestaurantClickListener
import com.example.TasteFinder.data.Restaurant
import com.squareup.picasso.Picasso

class TopFavoritesAdapter(private val restaurants: List<Restaurant>, private val listener: OnRestaurantClickListener) : RecyclerView.Adapter<TopFavoritesAdapter.FavoriteViewHolder>() {

    class FavoriteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val restaurantImage: ImageView = itemView.findViewById(R.id.iv_restaurant_image)
        val restaurantName: TextView = itemView.findViewById(R.id.tv_restaurant_name)
        val favoriteCount: TextView = itemView.findViewById(R.id.tv_favorite_count)
        fun bind(restaurant: Restaurant, listener: OnRestaurantClickListener) {
            itemView.setOnClickListener {
                listener.onRestaurantClick(restaurant)
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_favorite, parent, false)
        return FavoriteViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        val restaurant = restaurants[position]
        holder.restaurantName.text = restaurant.name
        holder.favoriteCount.text = restaurant.likes.toString()
        Picasso.get().load(restaurant.imageURL).into(holder.restaurantImage)

        holder.bind(restaurant, listener)
    }

    override fun getItemCount(): Int {
        return restaurants.size
    }
}