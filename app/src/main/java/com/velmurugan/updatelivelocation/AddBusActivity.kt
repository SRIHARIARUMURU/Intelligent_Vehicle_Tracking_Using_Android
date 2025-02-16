package com.velmurugan.updatelivelocation

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
//import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import com.google.firebase.database.*

class AddBusActivity : AppCompatActivity() {
    private lateinit var reference: DatabaseReference
    private lateinit var routeSpinner:Spinner
    private lateinit var driverNameEdittext:EditText
    private lateinit var driverMobileEdittext:EditText
    private lateinit var driverbusNoEdittext:EditText
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_bus)
        reference = FirebaseDatabase.getInstance().reference

        routeSpinner = findViewById(R.id.selectRouteSpinner)
        driverNameEdittext = findViewById(R.id.driverNameEdittext)
        driverMobileEdittext = findViewById(R.id.driverMobileNoEdittext)
        driverbusNoEdittext = findViewById(R.id.busnoEdittext)
        findViewById<Button>(R.id.saveBusButton).setOnClickListener {
            saveBusInfo()
        }

    }

    private fun saveBusInfo() {
        val busRoute = routeSpinner.selectedItem.toString()
        val driverName = driverNameEdittext.text.toString()
        val driverMobile = driverMobileEdittext.text.toString()
        val driverbusNo = driverbusNoEdittext.text.toString()

        if(routeSpinner.selectedItemPosition == 0){
            Toast.makeText(this,"Please Select the Bus Route..",Toast.LENGTH_LONG).show()
        }else if(driverName.isEmpty() || driverMobile.isEmpty() || driverbusNo.isEmpty()){
            Toast.makeText(this,"Please Fill the details...",Toast.LENGTH_LONG).show()
        }else{

            reference.child("Busses").child(busRoute).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    var checkup = false
                    var str:String = ""
                    val children = snapshot.children
                    for (data in children) {
                        val bus: MyBus = data.getValue(MyBus::class.java)!!
                        if (bus.driverMobileNo == driverMobile) {
                            checkup = true
                            str = driverMobile
                            break
                        }else if(bus.driverBusNo==driverbusNo){
                            checkup = true
                            str = driverbusNo
                            break
                        }else if(bus.routeNo==busRoute){
                            checkup = true
                            str = busRoute
                            break
                        }else {
                            checkup = false
                        }
                        //Log.d(TAG, "onDataChange: ${user.mobileno} $checkup")

                    }
                    if (!checkup) {
                        //Toast.makeText(baseContext,"sucess",Toast.LENGTH_LONG).show()
                        val bus = MyBus(busRoute,driverName,driverMobile,driverbusNo)
                        reference.child("Busses").child(busRoute).push().setValue(bus).addOnSuccessListener {
                            Toast.makeText(this@AddBusActivity,"Data Saved...",Toast.LENGTH_LONG).show()
                            routeSpinner.setSelection(0)
                            driverNameEdittext.setText("")
                            driverMobileEdittext.setText("")
                            driverbusNoEdittext.setText("")
                        }

                    } else {
                        Toast.makeText(baseContext, "$str Exist", Toast.LENGTH_LONG).show()
                    }

                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })



        }
    }
}