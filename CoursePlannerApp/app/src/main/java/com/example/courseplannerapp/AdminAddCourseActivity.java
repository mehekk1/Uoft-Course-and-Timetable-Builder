package com.example.courseplannerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.example.courseplannerapp.databinding.ActivityAdminAddCourseBinding;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class AdminAddCourseActivity extends AppCompatActivity {
    ActivityAdminAddCourseBinding binding;
    String code, name;
    boolean fall, winter, summer;
    List<Course> prereqs;
    FirebaseDatabase database;
    DatabaseReference ref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminAddCourseBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setContentView(R.layout.activity_admin_add_course);
        binding.adminAddCourseBtn.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                code = binding.adminAddCourseCode.getText().toString();
                name = binding.adminAddCourseName.getText().toString();

            }
        }));
    }
}