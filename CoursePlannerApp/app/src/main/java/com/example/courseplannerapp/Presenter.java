package com.example.courseplannerapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Presenter {
    LoginTabFragment view;
    Model model;
    public Presenter(LoginTabFragment view){
        this.view = view;
        this.model = new Model(this);
    }

    public void Login() {
        String email = view.getEmail();
        if(email == null || email.equals("")) {
            view.usernameError();
        }
        String pass = view.getPass();
        if(pass == null || pass.equals("")) {
            view.passwordError();
        }
        model.Login(email, pass);
    }

    public void Success(String CurrentUser, boolean isStudent){

        SharedPreferences sp;
        sp = view.getActivity().getSharedPreferences("save", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("UID",CurrentUser);
        editor.apply();

        view.loginSuccess(isStudent);
    }

    public void Failure(Exception e){
        Toast.makeText(view.getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
    }
}

