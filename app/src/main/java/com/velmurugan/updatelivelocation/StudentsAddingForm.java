package com.velmurugan.updatelivelocation;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class StudentsAddingForm extends AppCompatActivity {
    Spinner sp_branch,sp_year,sp_semister,sp_section, sp_busno;
    EditText et_rollno,et_name,et_mobile,et_email;
    DatabaseReference reference;
    RadioButton r_male,r_female;
    String gender;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_students_adding_form);
        sp_branch = findViewById(R.id.studentbranchSpinner);
        sp_year = findViewById(R.id.studentYesrSpinner);
        sp_semister = findViewById(R.id.studentSemisterSpinner);
        sp_section = findViewById(R.id.studentSectionSpinner);
        sp_busno=findViewById(R.id.busNumberSpinner);
        et_rollno = findViewById(R.id.studentRollno);
        et_name = findViewById(R.id.studentName);
        et_mobile = findViewById(R.id.studentMobileno);
        et_email = findViewById(R.id.studentEmailid);
        r_male = findViewById(R.id.studentMaleRadioButton);
        r_female = findViewById(R.id.studentFemaleRedioButton);

        reference = FirebaseDatabase.getInstance().getReference();

    }

    public void submitStudent(View view) {
        String sBranch = sp_branch.getSelectedItem().toString();
        String sYear = sp_year.getSelectedItem().toString();
        String sSemister = sp_semister.getSelectedItem().toString();
        String sSection = sp_section.getSelectedItem().toString();
        String sBusNo = sp_busno.getSelectedItem().toString(); // Corrected this line
        String sRollno = et_rollno.getText().toString();
        String sName = et_name.getText().toString();
        String sMobile = et_mobile.getText().toString();
        String sEmail = et_email.getText().toString();
        String sPassword = sRollno;

        if (r_male.isChecked()) {
            gender = r_male.getText().toString();
        } else if (r_female.isChecked()) {
            gender = r_female.getText().toString();
        }

        if (sp_branch.getSelectedItemPosition() == 0) {
            Toast.makeText(this, "Please Select the Branch", Toast.LENGTH_SHORT).show();
        } else if (sp_year.getSelectedItemPosition() == 0) {
            Toast.makeText(this, "Please Select the Year", Toast.LENGTH_SHORT).show();
        } else if (sp_semister.getSelectedItemPosition() == 0) {
            Toast.makeText(this, "Please Select the Semester", Toast.LENGTH_SHORT).show();
        } else if (sp_section.getSelectedItemPosition() == 0) {
            Toast.makeText(this, "Please select the Section", Toast.LENGTH_SHORT).show();
        } else if (sp_busno.getSelectedItemPosition() == 0) {
            Toast.makeText(this, "Please select the Bus Number", Toast.LENGTH_SHORT).show();
        } else if (sRollno.isEmpty() || sName.isEmpty() || sMobile.isEmpty() || sEmail.isEmpty() || sPassword.isEmpty() || gender.isEmpty()) {
            Toast.makeText(this, "Please fill in all details...", Toast.LENGTH_SHORT).show();
        } else {
            Student student = new Student(sBranch, sYear, sSemister, sSection, sBusNo, sRollno, sName, sMobile, sEmail, sPassword, gender);
            reference.child("Students").push().setValue(student).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(StudentsAddingForm.this, "Data Inserted successfully..", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}