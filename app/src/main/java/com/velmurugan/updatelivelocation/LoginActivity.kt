package com.velmurugan.updatelivelocation

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*

class LoginActivity : AppCompatActivity() {
    private lateinit var reference: DatabaseReference
    private lateinit var usernameEdittext: EditText
    private lateinit var passwordEdittext: EditText
    private lateinit var preferences: SharedPreferences
    private var usertypeSpinner: Spinner? = null
    private var studentList: ArrayList<Student?>? = null

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        reference = FirebaseDatabase.getInstance().reference
        preferences = getSharedPreferences("BUSTRACK_PREFRENCES", MODE_PRIVATE)
        usernameEdittext = findViewById(R.id.usernameEdittext)
        passwordEdittext = findViewById(R.id.passwordEdittext)
        usertypeSpinner = findViewById(R.id.userTypeSpinner)
        findViewById<Button>(R.id.loginButton).setOnClickListener {
            doLogin();
        }
        /*findViewById<Button>(R.id.studentButton).setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }*/
    }

    private fun doLogin() {
        val userName = usernameEdittext.text.toString();
        val password = passwordEdittext.text.toString();
        val userType = usertypeSpinner!!.selectedItem.toString()
        if (usertypeSpinner?.selectedItemPosition == 0) {
            Toast.makeText(this, "Please select the USertype", Toast.LENGTH_SHORT).show()
        } else if (userName.isEmpty() || password.isEmpty() || userType.isEmpty()) {
            Toast.makeText(this, "Please fill the details...", Toast.LENGTH_SHORT).show()
        } else {
            if (userType == "Admin") {
                if (userName == "Admin" && password == "1234") {
                    startActivity(Intent(this@LoginActivity, AdminActivity::class.java))
                }
            } else if (userType == "Student") {
                studentList = ArrayList()
                reference.child("Students")
                    .addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            for (dataSnapshot in snapshot.children) {
                                val s = dataSnapshot.getValue(Student::class.java)
                                studentList?.add(s)
                                if (userName == s!!.getsRollno() && password == s!!.getsMobile()) {
                                    val i = Intent(
                                        this@LoginActivity,
                                        MainActivity::class.java
                                    )
                                    startActivity(i)
                                    finish()
                                    break
                                }
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {}
                    })



               /* if (userName.isEmpty() || password.isEmpty()) {
                    Toast.makeText(this, "Please Fill the details...", Toast.LENGTH_LONG).show()
                } else {
                    if (userName == "Admin" && password == "1234") {
                        startActivity(Intent(this, AdminActivity::class.java))
                    } else {
                        reference.child("Busses").child(userName)
                            .addListenerForSingleValueEvent(object : ValueEventListener {
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    var checkup = false
                                    var str: String = ""
                                    val children = snapshot.children
                                    for (data in children) {
                                        val bus: MyBus = data.getValue(MyBus::class.java)!!
                                        if (bus.routeNo == userName && bus.driverMobileNo == password) {
                                            startActivity(
                                                Intent(
                                                    this@LoginActivity,
                                                    Driveractivity::class.java
                                                )
                                            )
                                            val editor = preferences.edit()
                                            editor.putString("routeNo", bus.routeNo)
                                            editor.putString("driverName", bus.driverName)
                                            editor.putString("driverMobile", bus.driverMobileNo)
                                            editor.putString("driverBusNo", bus.driverBusNo)
                                            editor.apply()
                                            break
                                        }
                                    }
                                }

                                override fun onCancelled(error: DatabaseError) {
                                    TODO("Not yet implemented")
                                }

                            })
                    }
                }*/


            }else if(userType == "Driver"){
                reference.child("Busses").child(userName)
                    .addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            var checkup = false
                            var str: String = ""
                            val children = snapshot.children
                            for (data in children) {
                                val bus: MyBus = data.getValue(MyBus::class.java)!!
                                if (bus.routeNo == userName && bus.driverMobileNo == password) {
                                    startActivity(
                                        Intent(
                                            this@LoginActivity,
                                            Driveractivity::class.java
                                        )
                                    )
                                    val editor = preferences.edit()
                                    editor.putString("routeNo", bus.routeNo)
                                    editor.putString("driverName", bus.driverName)
                                    editor.putString("driverMobile", bus.driverMobileNo)
                                    editor.putString("driverBusNo", bus.driverBusNo)
                                    editor.apply()
                                    break
                                }
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            TODO("Not yet implemented")
                        }

                    })
            }
        }
    }

}