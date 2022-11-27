package com.example.courseplannerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AdminWelcomeActivity extends AppCompatActivity {
    private Button admin_welcome_add_courses_btn;
    private Button admin_welcome_edit_courses_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_welcome);

        admin_welcome_add_courses_btn = (Button) findViewById(R.id.admin_welcome_add_btn);
        admin_welcome_edit_courses_btn = (Button) findViewById(R.id.admin_welcome_edit_btn);

        admin_welcome_add_courses_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAddCoursePage();
            }
        });

        admin_welcome_edit_courses_btn.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openEditCoursePage();
            }
        }));

    }
    public void openAddCoursePage(){
        Intent intent = new Intent(this,AdminAddCourse.class);
        startActivity(intent);
    }
    public void openEditCoursePage(){
    }

}