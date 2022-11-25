package com.example.courseplannerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class HomepageActivity extends AppCompatActivity {

    //fields
    private Button btnLogin;
    private Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        //change of pages
        btnLogin = (Button) findViewById(R.id.home_login_btn);
        btnRegister = (Button) findViewById(R.id.home_register_btn);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openLoginPage();
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openRegisterPage();
            }
        });


    }

    public void openLoginPage() {
        Intent intent = new Intent(this, StudentLoginActivity.class);
        startActivity(intent);
    }

    public void openRegisterPage(){
        Intent intent = new Intent(this, StudentSignupActivity.class);
        startActivity(intent);
    }
}