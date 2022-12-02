package com.example.courseplannerapp;

import static com.example.courseplannerapp.R.color.black;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ViewAllCoursesActivity extends AppCompatActivity {

    ArrayList<Course> courses = new ArrayList<>();
    FirebaseDatabase mDatabase;
    DatabaseReference mReferenceCourses;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_courses);
        context = this.getApplicationContext();

        mDatabase = FirebaseDatabase.getInstance();
        mReferenceCourses = mDatabase.getReference("CoursesTestVedat");

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



}
