package com.example.courseplannerapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.ViewGroup;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class StudentWelcomeActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_welcome);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation_view);

        bottomNav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch(id){
                    case R.id.nav_home:
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

    }

    public void openTimelinePage(){
        Intent intent = new Intent(this, GeneratedTimelineActivity.class);
        startActivity(intent);
    }

    public void openTakenTimelinePage(){
        Intent intent = new Intent(this, TakenTimelineActivity.class);
        startActivity(intent);
    }


//    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
//        @Override
//        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//            androidx.fragment.app.Fragment selectedFragment = null;
//
//            switch (item.getItemId()){
//                case R.id.nav_home:
//                    selectedFragment = new androidx.fragment.app.Fragment();
//                    break;
//            }
//
//            getSupportFragmentManager().beginTransaction().replace((R.id.fragment_container, selectedFragment));
//            return true;
//        }
//    };

}