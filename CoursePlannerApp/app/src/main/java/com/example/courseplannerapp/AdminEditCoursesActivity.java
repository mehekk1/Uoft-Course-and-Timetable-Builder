package com.example.courseplannerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AdminEditCoursesActivity extends AppCompatActivity {

    ArrayList<Course> editableCourses = new ArrayList<>();
    FirebaseDatabase DB;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_edit_courses);

    }

    private void setUpCourses(){
        DB = FirebaseDatabase.getInstance();
        reference = DB.getReference();


    }
}