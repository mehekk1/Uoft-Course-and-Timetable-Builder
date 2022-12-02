package com.example.courseplannerapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AdminEditCoursesActivity extends AppCompatActivity{

    ArrayList<Course> editableCourses = new ArrayList<>();
    FirebaseDatabase mDatabase;
    DatabaseReference mReferenceCourses;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_edit_courses);
        context = this.getApplicationContext();

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        int colorCodeDark = Color.parseColor("#FF000000");
        window.setStatusBarColor(colorCodeDark);

        mDatabase = FirebaseDatabase.getInstance();
        mReferenceCourses = mDatabase.getReference("CoursesTestVedat");

        RecyclerView recyclerView = findViewById(R.id.adminEditRecyclerView);
        AdminEditCoursesAdapterRecyclerView adapter = new AdminEditCoursesAdapterRecyclerView(this, editableCourses);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

//        Click listener for cards
        ItemClickSupport.addTo(recyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                if(position != RecyclerView.NO_POSITION){
                    openWelcomePage();
                }
            }
        });


        //        GET DATA
        mReferenceCourses.orderByKey().addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Course course = snapshot.getValue(Course.class);
                editableCourses.add(course);
                AdminEditCoursesAdapterRecyclerView adapter = new AdminEditCoursesAdapterRecyclerView(context, editableCourses);
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

    private void openWelcomePage(){
        Intent intent = new Intent(this, StudentWelcomeActivity.class);
        startActivity(intent);
    }

}