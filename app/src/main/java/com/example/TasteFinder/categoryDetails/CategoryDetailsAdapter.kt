package com.example.TasteFinder.categoryDetails

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

class CategoryDetailsAdapter (private var restaurants:List<Restaurant>, private val listener: OnRestaurantClickListener):
    RecyclerView.Adapter<CategoryDetailsAdapter.SearchViewHolder>() {

    class SearchViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val restaurantName: TextView = itemView.findViewById(R.id.details_name)
        val restaurantImage: ImageView = itemView.findViewById(R.id.details_image)
        fun bind(restaurant: Restaurant, listener: OnRestaurantClickListener) {
            itemView.setOnClickListener {
                listener.onRestaurantClick(restaurant)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_category_details, parent, false)
        return SearchViewHolder(view)
    }

    override fun getItemCount(): Int {
        return restaurants.size
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        val restaurant = restaurants[position]
        holder.restaurantName.text = restaurant.name
        Picasso.get().load(restaurant.imageURL).into(holder.restaurantImage)

        holder.bind(restaurant, listener)

    }
    fun updateList(newList: List<Restaurant>) {
        restaurants = newList
        notifyDataSetChanged() // Notify the adapter to refresh the views
    }
}