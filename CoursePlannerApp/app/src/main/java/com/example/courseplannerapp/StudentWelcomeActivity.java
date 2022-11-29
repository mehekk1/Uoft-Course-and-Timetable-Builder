package com.example.courseplannerapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class StudentWelcomeActivity extends AppCompatActivity{

    private BottomNavigationView bottomNav;
    private CardView homeCard;
    private CardView libraryCard;
    private CardView courseCard;
    private CardView timelineCard;
    private CardView settingsCard;
    private CardView logoutCard;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_welcome);

        bottomNav = findViewById(R.id.bottom_navigation_view);

        homeCard = findViewById(R.id.homeWidget);
        libraryCard = findViewById(R.id.libraryWidget);
        courseCard = findViewById(R.id.courseWidget);
        timelineCard = findViewById(R.id.timelineWidget);
        settingsCard = findViewById(R.id.settingsWidget);
        logoutCard = findViewById(R.id.logoutWidget);


        homeCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToLink("https://github.com/AshwinM1523/b07FinalProject");
            }
        });

//      Library

        courseCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openTakenTimelinePage();
            }
        });

        timelineCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openTimelinePage();
            }
        });

//      Settings

        logoutCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToLoginPage();
            }
        });

        bottomNav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch (id) {
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

    private void goToLink(String link){
        Uri url = Uri.parse(link);
        startActivity(new Intent(Intent.ACTION_VIEW, url));
    }

    private void openTimelinePage () {
        Intent intent = new Intent(this, FutureCoursesActivity.class);
        startActivity(intent);
    }

    private void openTakenTimelinePage () {
        Intent intent = new Intent(this, TakenTimelineActivity.class);
        startActivity(intent);
    }

    private void goToLoginPage(){
        Intent intent = new Intent(this, StudentLoginActivity.class);
        startActivity(intent);
    }


}