package com.example.TasteFinder.categories

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.TasteFinder.data.Category
import com.example.TasteFinder.R
import com.example.TasteFinder.data.OnCategoryClickListener
import com.example.TasteFinder.data.OnRestaurantClickListener

class CategoriesAdapter(private val categoryList: List<Category>, private val listener: OnCategoryClickListener) :
    RecyclerView.Adapter<CategoriesAdapter.CategoryViewHolder>() {

    class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val categoryName: TextView = itemView.findViewById(R.id.category_name)
        val categoryIcon: ImageView = itemView.findViewById(R.id.category_icon)
        fun bind(category: Category, listener: OnCategoryClickListener) {
            itemView.setOnClickListener {
                listener.onCategoryClick(category)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_category, parent, false)
        return CategoryViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val currentCategory = categoryList[position]
        holder.categoryName.text = currentCategory.name
        holder.categoryIcon.setImageResource(currentCategory.iconResId)
        holder.bind(currentCategory, listener)
    }

    override fun getItemCount() = categoryList.size
}