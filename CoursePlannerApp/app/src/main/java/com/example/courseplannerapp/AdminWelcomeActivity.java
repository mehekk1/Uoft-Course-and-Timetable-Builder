package com.example.courseplannerapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class AdminWelcomeActivity extends AppCompatActivity {

    private BottomNavigationView bottomAdminNav;
    private CardView homeAdminCard;
    private CardView libraryAdminCard;
    private CardView courseAdminCard;
    private CardView editAdminCard;
    private CardView settingsAdminCard;
    private CardView logoutAdminCard;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_welcome);

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        int colorCodeDark = Color.parseColor("#FF000000");
        window.setStatusBarColor(colorCodeDark);

        bottomAdminNav = findViewById(R.id.bottom_navigation_view);

        homeAdminCard = findViewById(R.id.homeAdminWidget);
        libraryAdminCard = findViewById(R.id.libraryWidget);
        courseAdminCard = findViewById(R.id.courseAdminWidget);
        editAdminCard = findViewById(R.id.editAdminWidget);
        settingsAdminCard = findViewById(R.id.settingsAdminWidget);
        logoutAdminCard = findViewById(R.id.logoutAdminWidget);

        bottomAdminNav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch (id) {
                    case R.id.nav_home:
                        break;
                    case R.id.nav_timeline:
                        openAddingCoursesPage();
                        break;
                    case R.id.nav_add_menu:
                        openEditCoursesPage();
                        break;
                }
                return true;
            }
        });

        homeAdminCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToLink("https://github.com/AshwinM1523/b07FinalProject");
            }
        });

        libraryAdminCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openEditCoursesPage();
            }
        });

        courseAdminCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAdminAddCoursesPage();
            }
        });

        editAdminCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openEditCoursesPage();
            }
        });

        settingsAdminCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openStatsPage();
            }
        });

        logoutAdminCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToLoginPage();
            }
        });
    }

    private void openAddingCoursesPage(){
        Intent intent = new Intent(this, AdminAddCourseActivity.class);
        startActivity(intent);
    }


    private void openEditCoursesPage(){
        Intent intent = new Intent(this, AdminEditCoursesActivity.class);
        startActivity(intent);
    }

    private void goToLink(String link){
        Uri url = Uri.parse(link);
        startActivity(new Intent(Intent.ACTION_VIEW, url));
    }


    private void openAdminAddCoursesPage(){
        Intent intent = new Intent(this, AdminAddCourseActivity.class);
        startActivity(intent);
    }

    private void goToLoginPage(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    private void openStatsPage(){
        Intent intent = new Intent(this, AdminStatsActivity.class);
        startActivity(intent);
    }


}