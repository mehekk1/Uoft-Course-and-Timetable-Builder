package com.example.courseplannerapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class AdminEditCoursesActivity extends AppCompatActivity{

    ArrayList<Course> editableCourses = new ArrayList<>();
    FirebaseDatabase mDatabase;
    DatabaseReference mReferenceCourses;
    Context context;
    private BottomNavigationView bottomAdminNav;

//    private SharedPreferences sharedPreferences;
//    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_edit_courses);

        context = this.getApplicationContext();

        bottomAdminNav = findViewById(R.id.bottom_navigation_view);

//        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
//        editor = sharedPreferences.edit();

        bottomAdminNav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch (id) {
                    case R.id.nav_home:
                        openWelcomePage();
                        break;
                    case R.id.nav_timeline:
                        openAdminAddCoursesPage();
                        break;
                    case R.id.nav_add_menu:
                        break;
                }
                return true;
            }
        });

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

                    Intent intent = new Intent(context, EditCourseActivity.class);
                    intent.putExtra("courseCode", editableCourses.get(position).code);
                    startActivity(intent);
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
        Intent intent = new Intent(this, AdminWelcomeActivity.class);
        startActivity(intent);
    }

    private void openAdminAddCoursesPage(){
        Intent intent = new Intent(this, AdminAddCourseActivity.class);
        startActivity(intent);
    }

//    public void openEditPage(){
//        Intent intent = new Intent(this, EditCourseActivity.class);
//        startActivity(intent);
//    }

}