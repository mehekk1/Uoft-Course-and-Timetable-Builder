package com.example.courseplannerapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class EditCourseActivity extends AppCompatActivity {

    ArrayList<Course> editableCourses = new ArrayList<>();
    FirebaseDatabase mDatabase;
    DatabaseReference mReferenceCourses;

    EditText courseEditName, courseEditCode;
    Switch switchFall, switchSummer, swichWinter;
    Button editBtn;

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_course);
        context = getApplicationContext();

        courseEditName = findViewById(R.id.edit_course_name_txt);
        courseEditCode = findViewById(R.id.edit_course_code_txt);
        switchFall = findViewById(R.id.edit_fall_switch);
        switchSummer = findViewById(R.id.edit_summer_switch);
        swichWinter = findViewById(R.id.edit_winter_switch);
        editBtn = findViewById(R.id.confirm_edit_btn);


        Intent incomingIntent = getIntent();
        String incomingCourseCode = incomingIntent.getStringExtra("courseCode");

        mDatabase = FirebaseDatabase.getInstance();
        mReferenceCourses = mDatabase.getReference("CoursesTestVedat");
        mReferenceCourses.child(incomingCourseCode).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {

                DataSnapshot dataSnapshot = task.getResult();
                Course course = dataSnapshot.getValue(Course.class);

                courseEditName.setText(course.name);
                courseEditCode.setText(course.code);

                swichWinter.setChecked(course.offerings.get(0));
                switchSummer.setChecked(course.offerings.get(1));
                switchFall.setChecked(course.offerings.get(2));


                editBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(!(swichWinter.isChecked() || switchFall.isChecked() || switchSummer.isChecked())){
                            Toast.makeText(context, "Must have atleast one offering", Toast.LENGTH_SHORT).show();
                        }


                        String newCourseName = courseEditName.getText().toString();
                        String newCourseCode = courseEditCode.getText().toString();


                    }
                });


            }
        });
    }
}
