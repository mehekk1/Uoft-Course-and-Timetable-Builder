package com.example.courseplannerapp;

import static com.example.courseplannerapp.R.color.black;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ViewAllCoursesActivity extends AppCompatActivity {

    private BottomNavigationView bottomStudentNav;
    ArrayList<Course> courses = new ArrayList<>();
    FirebaseDatabase mDatabase;
    DatabaseReference mReferenceCourses;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_courses);
        context = this.getApplicationContext();
        bottomStudentNav = findViewById(R.id.bottom_navigation_view);

//        Navigation bar
        bottomStudentNav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch (id) {
                    case R.id.nav_home:
                        openHomePage();
                        break;
                    case R.id.nav_timeline:
                        openTimelinePage();
                        break;
                    case R.id.nav_add_menu:
                        openTakenTimelinePage();
                        break;
                }
                return true;
            }
        });

        mDatabase = FirebaseDatabase.getInstance();
        mReferenceCourses = mDatabase.getReference("Courses");

        RecyclerView recyclerView = findViewById(R.id.viewAllCoursesRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        ViewAllCoursesAdapter adapter = new ViewAllCoursesAdapter(this, courses);
        recyclerView.setAdapter(adapter);

        mReferenceCourses.orderByKey().addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Course course = snapshot.getValue(Course.class);
                courses.add(course);
                ViewAllCoursesAdapter adapter = new ViewAllCoursesAdapter(context, courses);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void openHomePage(){
        Intent intent = new Intent(this, StudentWelcomeActivity.class);
        startActivity(intent);
    }

    private void openTimelinePage () {
        Intent intent = new Intent(this, FutureCoursesActivity.class);
        startActivity(intent);
    }


    private void openTakenTimelinePage () {
        Intent intent = new Intent(this, TakenTimelineActivity.class);
        startActivity(intent);
    }

}
