package com.example.TasteFinder.data

import com.google.firebase.firestore.PropertyName
import java.io.Serializable

data class Restaurant(
    val id: String = "",
    @PropertyName("Name") val name: String = "",
    @PropertyName("Location") val location: String = "",
    @PropertyName("Menu") val menu: List<String> = emptyList(),
    @PropertyName("PhoneNumbers") val phoneNumbers: List<String> = emptyList(),
    @PropertyName("Categories") val categories: List<String> = emptyList(),
    @PropertyName("Likes") val likes: Int = 0,
    @PropertyName("ImageURL") val imageURL: String = "",
    @PropertyName("FacebookLink") val facebookLink: String = ""
): Serializable
