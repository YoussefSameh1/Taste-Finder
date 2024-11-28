import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.TasteFinder.R
import com.example.TasteFinder.data.Restaurant
import com.squareup.picasso.Picasso

class FavoritesAdapter(
    private var items: List<Restaurant>,  // Change 'val' to 'var' to allow updating the list
    private val isCategory: Boolean = false, // Flag to distinguish use case
    private val onFavoriteClick: ((Restaurant) -> Unit)? = null // Optional click handler
) : RecyclerView.Adapter<FavoritesAdapter.FavoritesViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoritesViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fav_restaurant, parent, false)
        return FavoritesViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavoritesViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    // Method to update the categories and notify the adapter
    fun updateCategories(newItems: List<Restaurant>) {
        items = newItems  // Update the list of items
        notifyDataSetChanged()  // Notify the adapter to refresh the view
    }

    inner class FavoritesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val logo: ImageView = itemView.findViewById(R.id.logo)
        private val name: TextView = itemView.findViewById(R.id.name)
        private val favoriteIcon: ImageView = itemView.findViewById(R.id.favorite)

        fun bind(restaurant: Restaurant) {
            name.text = restaurant.name
            Picasso.get().load(restaurant.imageURL).into(logo)
            // Hide favorite icon if it's a category
            if (isCategory) {
                favoriteIcon.visibility = View.GONE
            } else {
                favoriteIcon.visibility = View.VISIBLE
                favoriteIcon.setOnClickListener {
                    onFavoriteClick?.invoke(restaurant)
                }
            }
        }
    }
}
