package com.velmurugan.updatelivelocation

import android.Manifest
import android.annotation.SuppressLint
//import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapFragment
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.database.*


class MainActivity : AppCompatActivity(), ValueEventListener, OnMapReadyCallback {
    private lateinit var reference: DatabaseReference
    private lateinit var mapView: View
    private lateinit var mMap: GoogleMap
    private var lat: Double = 0.0
    private var lang: Double = 0.0
    private lateinit var spinner: Spinner

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        reference = FirebaseDatabase.getInstance().reference

        val getBusLocationbtn: Button = findViewById<Button>(R.id.getBuslocationButton)
        getBusLocationbtn.setOnClickListener {
            getBusLocation()
        }

        spinner = findViewById(R.id.studentrouteSpinner)
        if (intent.hasExtra("routeno")) {
            val rstr = intent.getStringExtra("routeno")
            spinner.visibility = View.GONE
            getBusLocationbtn.visibility = View.GONE
            reference.child("MyLocation").child(rstr!!).addValueEventListener(this)
        }

        val mapFragment = fragmentManager.findFragmentById(R.id.map_fragment) as MapFragment
        mapFragment.getMapAsync(this)
        mapView = mapFragment.view!!

        if (!checkPermission()) {
            requestPermission()
        }
    }

    private fun getBusLocation() {
        val routeno = spinner.selectedItem.toString()
        if (spinner.selectedItemPosition == 0) {
            Toast.makeText(this, "Please select the route...", Toast.LENGTH_LONG).show()
        } else {
            reference.child("MyLocation").child(routeno).addValueEventListener(this)
        }
    }

    private fun checkPermission(): Boolean {
        val result = ContextCompat.checkSelfPermission(
            applicationContext,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
        val result1 = ContextCompat.checkSelfPermission(
            applicationContext,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ),
            1
        )
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
    }

    private fun setMarkers(latLng: LatLng) {
        val markerOptions = MarkerOptions().position(latLng).title("I am here!")
        mMap.clear() // Clear the previous markers
        mMap.addMarker(markerOptions) // Add the new marker at the new location

        // Animate camera to the new position and apply zoom
        mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng))
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 20f)) // Zoom level set to 15
    }

    override fun onDataChange(snapshot: DataSnapshot) {
        // Retrieve latitude and longitude values from Firebase
        lang = snapshot.child("lang").value as Double
        lat = snapshot.child("lat").value as Double

        // Create a LatLng object from the updated location
        val updatedLocation = LatLng(lat, lang)

        // Set the marker and animate the camera with zoom
        setMarkers(updatedLocation)
    }

    override fun onCancelled(error: DatabaseError) {
        // Handle any errors here if needed
        Toast.makeText(this, "Failed to get data: ${error.message}", Toast.LENGTH_LONG).show()
    }
}
