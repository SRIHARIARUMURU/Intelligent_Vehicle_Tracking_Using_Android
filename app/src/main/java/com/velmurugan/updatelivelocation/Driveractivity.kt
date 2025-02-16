package com.velmurugan.updatelivelocation

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class Driveractivity : AppCompatActivity() {
    private lateinit var preferences: SharedPreferences
    private lateinit var routeNo:String
    private lateinit var driverName:String
    private lateinit var driverMobileNo:String
    private lateinit var driverBusNo:String
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_driveractivity)
        val buttonStartService = findViewById<Button>(R.id.buttonStartService)
        val buttonStopService = findViewById<Button>(R.id.buttonStopService)
       /* editor.putString("routeNo",bus.routeNo)
        editor.putString("driverName",bus.driverName)
        editor.putString("driverMobile",bus.driverMobileNo)
        editor.putString("driverBusNo",bus.driverBusNo)*/
        preferences = getSharedPreferences("BUSTRACK_PREFRENCES", MODE_PRIVATE)
        routeNo = preferences.getString("RouteNo","").toString()
        driverName = preferences.getString("driverName","").toString()
        driverMobileNo = preferences.getString("driverMobile","").toString()
        driverBusNo = preferences.getString("driverBusNo","").toString()

        buttonStartService.setOnClickListener {
            ContextCompat.startForegroundService(this, Intent(this, LocationService::class.java))
        }

        buttonStopService.setOnClickListener {
            stopService(Intent(this, LocationService::class.java))
        }

        if (!checkPermission()) {
            requestPermission()
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
}