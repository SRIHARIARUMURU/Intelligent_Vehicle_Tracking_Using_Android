package com.velmurugan.updatelivelocation

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*

class AdminActivity : AppCompatActivity() {

    lateinit var rv:RecyclerView
    lateinit var reference: DatabaseReference
    lateinit var bussesList:ArrayList<MyBus>
    lateinit var studentsList:ArrayList<Student>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)
        reference = FirebaseDatabase.getInstance().reference

        val addBussButton = findViewById<Button>(R.id.addBuss)
        val viewBussesButton = findViewById<Button>(R.id.viewBus)
        val addStudentButton = findViewById<Button>(R.id.addStudentsButton)
        val viewStudentButton = findViewById<Button>(R.id.viewStudentsButton)
        viewStudentButton.setOnClickListener{
            getStudentsData()
        }
        addStudentButton.setOnClickListener {
            startActivity(Intent(this,StudentsAddingForm::class.java))
        }

         rv = findViewById(R.id.rv_busseslist)
        rv.layoutManager = LinearLayoutManager(this)

        addBussButton.setOnClickListener {
            startActivity(Intent(this,AddBusActivity::class.java))
        }
        viewBussesButton.setOnClickListener {
            getBussesData()
        }

    }

    private fun getStudentsData() {
        studentsList = ArrayList()
        bussesList = ArrayList()
        reference.child("Students")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (dataSnapshot in snapshot.children) {
                        val s = dataSnapshot.getValue(Student::class.java)
                        if (s != null) {
                            studentsList?.add(s)
                        }
                        rv.adapter = BusAdapter(this@AdminActivity, bussesList, studentsList,reference)

                    }
                }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun getBussesData() {
        bussesList = ArrayList()
        studentsList = ArrayList()
        reference.child("Busses").addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for(data in snapshot.children){
                    for(i in data.children){
                        val bus = i.getValue(MyBus::class.java)

                        if (bus != null) {
                            bussesList.add(bus)
                        }
                    }
                    //Toast.makeText(this@AdminActivity,"$data",Toast.LENGTH_LONG).show()

                }
                rv.adapter = BusAdapter(this@AdminActivity,bussesList,studentsList,reference)

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

}