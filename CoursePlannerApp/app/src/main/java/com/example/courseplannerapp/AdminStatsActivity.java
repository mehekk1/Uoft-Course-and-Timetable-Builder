package com.example.courseplannerapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AdminStatsActivity extends AppCompatActivity {

    SharedPreferences getId;
    HashMap<String, Object> allCoursesDatabase;
    String user;
    FirebaseDatabase mDatabase;
    DatabaseReference mCourseRef;
    TextView allCourses;
    Button statsHomeBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_stats);

        allCourses = findViewById(R.id.total_courses);
        statsHomeBtn = findViewById(R.id.admin_stats_home_btn);
        statsHomeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToHomePage();
            }
        });

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        int colorCodeDark = Color.parseColor("#FF000000");
        window.setStatusBarColor(colorCodeDark);

        getId = getSharedPreferences("save", MODE_PRIVATE);
        user = getId.getString("UID", "defaultUser");
        mDatabase = FirebaseDatabase.getInstance();
        mCourseRef = mDatabase.getReference("Courses");

        mCourseRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                GenericTypeIndicator<HashMap<String, Object>> userCourses = new GenericTypeIndicator<HashMap<String, Object>>() {};
                allCoursesDatabase = task.getResult().getValue(userCourses);

                allCourses.setText("" + allCoursesDatabase.size());
                allCourses.setGravity(Gravity.CENTER);
            }
        });
    }

    private void goToHomePage(){
        Intent intent = new Intent(this, AdminWelcomeActivity.class);
        startActivity(intent);
    }
}