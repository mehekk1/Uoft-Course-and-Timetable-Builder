package com.example.courseplannerapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;

import java.util.ArrayList;

public class UserSettingsActivity extends AppCompatActivity {

    SharedPreferences getId;
    String student;
    FirebaseDatabase mDatabase;
    DatabaseReference mStudentRef;
    TextView takenCoursesStats, selectedCoursesStats;
    ArrayList<String> courses;
    ArrayList<String> selectedCourses;
    Button homeBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_settings);

        takenCoursesStats = findViewById(R.id.settings_taken_courses);
        selectedCoursesStats = findViewById(R.id.settings_selected_courses);
        homeBtn = findViewById(R.id.settings_page_go_back);

        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openHomePage();
            }
        });


        getId = getSharedPreferences("save", MODE_PRIVATE);
        student = getId.getString("UID", "defaultUser");
        mDatabase = FirebaseDatabase.getInstance();
        mStudentRef = mDatabase.getReference("Users");

        mStudentRef.child(student).child("taken_list").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                GenericTypeIndicator<ArrayList<String>> takenCourses = new GenericTypeIndicator<ArrayList<String>>(){};
                if (task.getResult().exists()){
                    courses = task.getResult().getValue(takenCourses);
                    takenCoursesStats.setText("You have taken this many courses so far: " + courses.size());
                }
                else{
                    takenCoursesStats.setText("You have taken this many courses so far: 0");
                }
                takenCoursesStats.setGravity(Gravity.CENTER);
            }
        });

        mStudentRef.child(student).child("coursesSelected").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                GenericTypeIndicator<ArrayList<String>> takenCourses = new GenericTypeIndicator<ArrayList<String>>(){};
                if (task.getResult().exists()){
                    selectedCourses = task.getResult().getValue(takenCourses);
                    selectedCoursesStats.setText("You have selected this many future courses: " + selectedCourses.size());
                }
                else{
                    selectedCoursesStats.setText("You have selected this many future courses: 0");
                }

                selectedCoursesStats.setGravity(Gravity.CENTER);
            }
        });
    }

    private void openHomePage(){
        Intent intent = new Intent(this, StudentWelcomeActivity.class);
        startActivity(intent);
    }

}