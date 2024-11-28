package com.example.TasteFinder

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.TasteFinder.data.User
import com.example.TasteFinder.home.HomeActivity
import com.example.TasteFinder.onBoarding.OnBoardingActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    private val LOCATION_PERMISSION_REQUEST_CODE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Check for location permission
        if (isLocationPermissionGranted()) {
            // If permission is already granted, proceed to the next activity
            startNextActivity()
        } else {
            // If permission is not granted, request permission
            requestLocationPermission()
        }
    }

    private fun startNextActivity() {
        val auth = FirebaseAuth.getInstance()
        if (auth.currentUser == null) {
            val intent = Intent(this, OnBoardingActivity::class.java)
            startActivity(intent)
            finish() // Call finish to remove MainActivity from the back stack
        }else{
            val intent = Intent(this, HomeActivity::class.java)
            intent.putExtra("userID",auth.currentUser?.uid)
            startActivity(intent)
            finish()
        }
    }

    private fun isLocationPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            LOCATION_PERMISSION_REQUEST_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, proceed to the next activity
                startNextActivity()
            } else {
                // Permission denied, handle accordingly (e.g., show a message)
                // You can also consider directing the user to settings or showing a Snackbar
            }
        }
    }
}
