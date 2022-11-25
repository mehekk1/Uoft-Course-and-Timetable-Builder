package com.example.courseplannerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class StudentWelcomeActivity extends AppCompatActivity {

    private Button welcome_page_view_courses_btn;
    private Button welcome_page_my_courses_btn;
    private Button welcome_page_timeline_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_welcome);

        welcome_page_my_courses_btn = (Button) findViewById(R.id.welcome_page_my_course_btn);
        welcome_page_timeline_btn = (Button) findViewById(R.id.welcome_page_timeline_btn);

        welcome_page_my_courses_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openMyCoursePage();
            }
        });

        welcome_page_timeline_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openTimelinePage();
            }
        });

    }

    public void openMyCoursePage(){
        Intent intent = new Intent(this, TakenTimelineActivity.class);
        startActivity(intent);
    }

    public void openTimelinePage(){
        Intent intent = new Intent(this, GeneratedTimelineActivity.class);
        startActivity(intent);
    }
}