package com.example.TasteFinder.categories

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.TasteFinder.R
import com.example.TasteFinder.categoryDetails.CategoryActivity
import com.example.TasteFinder.data.Category
import com.example.TasteFinder.data.OnCategoryClickListener
import com.example.TasteFinder.data.OnRestaurantClickListener
import com.example.TasteFinder.data.Restaurant
import com.example.TasteFinder.login.LoginActivity
import com.example.TasteFinder.restaurantDetails.RestaurantDetailsActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class CategoriesFragment : Fragment(), OnRestaurantClickListener, OnCategoryClickListener {
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_categories, container, false)
        val user = arguments?.getString("userID")
        Log.d("CategoriesFragmentUser", user.toString())
        val progressBar = view.findViewById<ProgressBar>(R.id.category_progressBar)
        val categoryLayout = view.findViewById<View>(R.id.category_layout)
        val signOutButton = view.findViewById<Button>(R.id.signOutButton)
        val helloCategory = view.findViewById<TextView>(R.id.hello_category)
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id)) // From Firebase console
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(requireContext(), gso)
        FirebaseFirestore.getInstance().collection("Users").document(user.toString()).get().addOnSuccessListener {
            document ->
            helloCategory.text = "Hello ${document.get("Name")}"
        }

        signOutButton.setOnClickListener {
            signOut()
        }

        progressBar.visibility = View.VISIBLE
        categoryLayout.visibility = View.GONE

        val topFavoritesRecyclerView = view.findViewById<RecyclerView>(R.id.rv_top_favorites)
        topFavoritesRecyclerView.layoutManager = LinearLayoutManager(
            requireContext(), LinearLayoutManager.HORIZONTAL, false
        )

        val restaurantsRef= FirebaseFirestore.getInstance().collection("Restaurants")
        restaurantsRef.orderBy("Likes", com.google.firebase.firestore.Query.Direction.DESCENDING)
            .limit(5).get().addOnSuccessListener {
                documents ->
                val restaurantList = mutableListOf<Restaurant>()
                for (document in documents) {
                    val restaurant = document.toObject(Restaurant::class.java)
                    restaurantList.add(restaurant.copy(id = document.id))
                }
                topFavoritesRecyclerView.adapter = TopFavoritesAdapter(restaurantList, this)
                progressBar.visibility = View.GONE
                categoryLayout.visibility = View.VISIBLE
            }


        val categoriesRecyclerView = view.findViewById<RecyclerView>(R.id.rv_categories)
        val spanCount = 3 // Number of rows
        val gridLayoutManager = GridLayoutManager(requireContext(), spanCount, GridLayoutManager.HORIZONTAL, false)
        categoriesRecyclerView.layoutManager = gridLayoutManager
        categoriesRecyclerView.adapter = CategoriesAdapter(getCategories(), this)

        return view
    }

    private fun signOut() {
        if (GoogleSignIn.getLastSignedInAccount(requireContext()) != null) {
            googleSignInClient.signOut().addOnCompleteListener(requireActivity()) {
                // Then sign out from Firebase
                FirebaseAuth.getInstance().signOut()
                updateUI() // Update your UI to reflect that the user has signed out
            }
        } else {
            // Email/Password Sign-In: sign out from Firebase
            FirebaseAuth.getInstance().signOut()
            updateUI() // Update your UI to reflect that the user has signed out
        }
    }

    private fun updateUI() {
        val intent = Intent(requireContext(), LoginActivity::class.java)
        startActivity(intent)
        requireActivity().finish() // Finish the current activity to prevent going back to the home screen

    }

    private fun getCategories(): List<Category> {
        return listOf(
            Category("Burger","Burger", R.drawable.burger),
            Category("Pasta","Pasta", R.drawable.pasta),
            Category("Pizza","Pizza", R.drawable.pizza),
            Category("Shawerma","Shawerma", R.drawable.shawerma),
            Category("SeeFood","Sea food", R.drawable.sea_food),
            Category("FriedChicken","Fried chicken", R.drawable.fried_chicken),
            Category("Dessert" ,"Dessert", R.drawable.dessert),
            Category("Cafe","Café", R.drawable.cafe),
            Category("Others","Others", R.drawable.other)
        )
    }

    override fun onRestaurantClick(restaurant: Restaurant) {
        val intent = Intent(requireContext(), RestaurantDetailsActivity::class.java)
        intent.putExtra("restaurant", restaurant)
        startActivity(intent)
    }

    override fun onCategoryClick(category: Category) {
        val intent = Intent(requireContext(), CategoryActivity::class.java)
        intent.putExtra("categoryID", category.id)
        startActivity(intent)
    }
}
