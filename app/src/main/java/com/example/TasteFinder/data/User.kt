package com.example.TasteFinder.data

import com.google.firebase.firestore.PropertyName
import java.io.Serializable

data class User(
    val id : String = "",
    @PropertyName("Name") val name : String = "",
    @PropertyName("Email") val email : String = "",
    @PropertyName("Favorites") val favorites : List<String> = emptyList()
): Serializable