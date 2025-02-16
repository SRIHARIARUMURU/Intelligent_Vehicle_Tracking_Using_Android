package com.velmurugan.updatelivelocation

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.content.SharedPreferences
import android.location.Location
//import android.location.LocationListener
import android.os.Build
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
//import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.*
//import retrofit2.Call
//import retrofit2.Callback
//import retrofit2.Response
import java.util.*

class LocationService : Service() {
    private val NOTIFICATION_CHANNEL_ID = "my_notification_location"
    private val TAG = "LocationService"
    private var lat:Double = 0.0
    private var lang:Double = 0.0
    private lateinit var preferences: SharedPreferences
    private lateinit var routeNo:String
    private lateinit var driverName:String
    private lateinit var driverMobileNo:String
    private lateinit var driverBusNo:String

    private lateinit var reference: DatabaseReference
    override fun onCreate() {
        super.onCreate()
        reference = FirebaseDatabase.getInstance().reference
        preferences = getSharedPreferences("BUSTRACK_PREFRENCES", MODE_PRIVATE)
        routeNo = preferences.getString("routeNo","").toString()
        driverName = preferences.getString("driverName","").toString()
        driverMobileNo = preferences.getString("driverMobile","").toString()
        driverBusNo = preferences.getString("driverBusNo","").toString()
        isServiceStarted = true
        Toast.makeText(applicationContext,"Service Started",Toast.LENGTH_LONG).show()
        val builder: NotificationCompat.Builder =
            NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                .setOngoing(false)
                .setSmallIcon(R.drawable.ic_launcher_background)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager: NotificationManager =
                getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            val notificationChannel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                NOTIFICATION_CHANNEL_ID, NotificationManager.IMPORTANCE_LOW
            )
            notificationChannel.description = NOTIFICATION_CHANNEL_ID
            notificationChannel.setSound(null, null)
            notificationManager.createNotificationChannel(notificationChannel)
            startForeground(1, builder.build())
        }
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        val timer = Timer()
        LocationHelper().startListeningUserLocation(
            this, object : MyLocationListener {
                override fun onLocationChanged(location: Location?) {
                    mLocation = location
                    mLocation?.let {
                        AppExecutors.instance?.networkIO()?.execute {
                            val apiClient = ApiClient.getInstance(this@LocationService)
                                .create(ApiClient::class.java)
                            val response = apiClient.updateLocation()
                            reference.child("MyLocation").child(routeNo).addListenerForSingleValueEvent(object :
                                ValueEventListener {
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    val latlang = snapshot.getValue(MyLatLang::class.java)
                                    if (latlang!=null) {
                                        Log.d(
                                            TAG,
                                            "onLocationChanged: Latitude ${it.latitude} , Longitude ${it.longitude}"
                                        )
                                        //  val lat = MyLatLang(it.latitude.toString(),it.longitude.toString())
                                        latlang?.lat = it.latitude
                                        latlang?.lang = it.longitude
                                        reference.child("MyLocation").child(routeNo)
                                            .setValue(latlang)
                                    }else{
                                        val latLang:MyLatLang = MyLatLang(it.latitude,it.longitude,routeNo,driverName,driverMobileNo,driverBusNo)
                                        reference.child("MyLocation").child(routeNo).setValue(latLang).addOnSuccessListener {
                                            Toast.makeText(applicationContext,"Data Updated",Toast.LENGTH_LONG).show()
                                        }
                                    }

                                }

                                override fun onCancelled(error: DatabaseError) {}
                            })

                        }
                    }
                }
            })
        return START_STICKY
    }

    /*fun saveData(){
        if(lat == 0.00 || lang == 0.00){
            //17.385044,78.486671
            val latLang:MyLatLang = MyLatLang(17.385044,78.486671,routeNo,driverName,driverMobileNo,driverBusNo)
            Log.d(TAG, "onLocationChanged: Latitude $lat , Longitude $lang")
            reference.child("MyLocation").child(routeNo).setValue(latLang).addOnSuccessListener {
                Toast.makeText(applicationContext,"Data Updated",Toast.LENGTH_LONG).show()
            }
        }else{
            val latLang:MyLatLang = MyLatLang(lat,lang,routeNo,driverName,driverMobileNo,driverBusNo)
            Log.d(TAG, "onLocationChanged: Latitude $lat , Longitude $lang")
            reference.child("MyLocation").child(routeNo).setValue(latLang).addOnSuccessListener {
                Toast.makeText(applicationContext,"Data Updated",Toast.LENGTH_LONG).show()
            }
        }


    }*/

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        isServiceStarted = false
       // LocationHelper().stopListeningUserLocation(applicationContext)
        //Toast.makeText(applicationContext,"Service Stopped",Toast.LENGTH_LONG).show()

    }

    companion object {
        var mLocation: Location? = null
        var isServiceStarted = false
    }
}