package com.velmurugan.updatelivelocation

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener

class BusAdapter(
    val context: Context,
    val bussesList: ArrayList<MyBus>,
    val studentsList: ArrayList<Student>,
    val reference: DatabaseReference
) : RecyclerView.Adapter<BusAdapter.ViewHolder>() {


    private val size =
        if (studentsList.size > 0) studentsList.size else if (bussesList.size > 0) bussesList.size else 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BusAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.bus_row, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (bussesList.size > 0) {
            // Bus Data
            holder.tv_route.text = "BusNo: ${bussesList[position].routeNo}"
            holder.tvdname.text = "Driver Name: ${bussesList[position].driverName}"
            holder.tvdmobile.text = "Driver MobileNo: ${bussesList[position].driverMobileNo}"
            holder.tvbusno.text = "Bus No: ${bussesList[position].driverBusNo}"
            holder.trackbtn.setOnClickListener {
                val i = Intent(context, MainActivity::class.java)
                i.putExtra("routeno", bussesList[position].routeNo)
                context.startActivity(i)
            }
            holder.delBtn.setOnClickListener {
                val RouteNo = bussesList[position].routeNo
                deletBus(RouteNo, position)
            }
            holder.tv_student_busno.visibility = View.GONE  // Hide Bus Number field for buses
        } else if (studentsList.size > 0) {
            // Student Data
            holder.tv_route.text = "Roll No: ${studentsList[position].getsRollno()}"
            holder.tvdname.text = "Student Name: ${studentsList[position].getsName()}"
            holder.tvdmobile.text = "Student MobileNo: ${studentsList[position].getsMobile()}"
            holder.tvbusno.text = "Student Year: ${studentsList[position].getsYear()}"
            holder.tv_student_busno.text = "Bus No: ${studentsList[position].getsBusNo()}" // Show Bus Number
            holder.tv_student_busno.visibility = View.VISIBLE // Make it visible
            holder.trackbtn.visibility = View.GONE
            holder.delBtn.setOnClickListener {
                val rollNo = studentsList[position].getsRollno()
                deleteStudent(rollNo, position)
            }
        } else {
            Toast.makeText(context, "Data Not Found", Toast.LENGTH_SHORT).show()
            holder.itemView.visibility = View.GONE
        }
    }


    private fun deletBus(routeNo: CharSequence?, position: Int) {
        reference.child("Busses").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var busFound = false  // Flag to track if bus is found
                for (data in snapshot.children) {
                    for (i in data.children) {
                        val bus = i.getValue(MyBus::class.java)

                        if (bus != null && routeNo == bus.routeNo) { // Check if routeno matches
                            val busKey = i.key  // Get the key of the bus node
                            busKey?.let {
                                // Delete the bus from the database by referencing the key
                                reference.child("Busses").child(data.key!!).child(it).removeValue()
                                Toast.makeText(context, "Bus with route number $routeNo deleted.", Toast.LENGTH_SHORT).show()
                                busFound = true
                               /* bussesList.removeAt(position)
                                notifyItemRemoved(position)*/
                            }
                            break  // Exit the loop once the bus is found and deleted
                        }
                    }
                    if (busFound) break  // Exit the outer loop as well
                }


                // Notify if no bus was found to delete
                if (!busFound) {
                    Toast.makeText(context, "Bus with route number $routeNo not found.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "Failed to delete bus. Error: ${error.message}", Toast.LENGTH_LONG).show()
            }
        })

    }

    private fun deleteStudent(rollNo: CharSequence, position: Int) {
        reference.child("Students").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var studentNotfound = false  // Flag to track if bus is found
                for (dataSnapshot in snapshot.children) {
                    val s = dataSnapshot.getValue(Student::class.java)
                    // Check if the student rollno matches
                    if (rollNo == s!!.getsRollno()) {
                        // The student is found, now delete from the Firebase database
                        val studentKey = dataSnapshot.key // Get the key of the student node
                        studentKey?.let {
                            // Reference the student's node using their key and remove it
                            reference.child("Students").child(it).removeValue()
                            Toast.makeText(
                                context,
                                "Student deleted successfully.",
                                Toast.LENGTH_SHORT
                            ).show()
                            studentNotfound = true
                           /* studentsList.removeAt(position)
                            notifyItemRemoved(position)*/
                            //context.startActivity(Intent(context,AdminActivity))
                        }

                        if (studentNotfound) break  // Exit the outer loop as well
                    }
                }


                // Notify if no bus was found to delete
                if (!studentNotfound) {
                    Toast.makeText(context, "student with roll number $rollNo not found.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "Failed to delete student. Error: ${error.message}", Toast.LENGTH_LONG).show()
            }
        })

    }

    override fun getItemCount(): Int {
        return size
    }
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val tv_route: TextView = itemView.findViewById(R.id.tv_route)
        val tvdname: TextView = itemView.findViewById(R.id.tv_dname)
        val tvdmobile: TextView = itemView.findViewById(R.id.tv_dMobileno)
        val tvbusno: TextView = itemView.findViewById(R.id.tv_dbusno)
        val tv_student_busno: TextView = itemView.findViewById(R.id.tv_student_busno) // New field for bus number
        val trackbtn: Button = itemView.findViewById(R.id.btn_track)
        val delBtn: Button = itemView.findViewById(R.id.recordDelete)
    }



}
