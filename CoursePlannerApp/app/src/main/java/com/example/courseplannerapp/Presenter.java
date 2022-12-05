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
            view.Email.setError("This field cannot be empty."); // this error message will be shown.
            return;
        }
        String pass = view.getPass();
        if(pass == null || pass.equals("")) {
            view.Pass.setError("This field cannot be empty."); // this error message will be shown.
            return;
        }
        model.Login(email, pass);
    }

    public void addUserSharedPref(String CurrentUser){

        SharedPreferences sp;
        sp = view.getActivity().getSharedPreferences("save", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("UID",CurrentUser);
        editor.apply();
    }

    public void Success(boolean isStudent){
        if (isStudent){
            view.getActivity().startActivity(new Intent(view.getActivity(), StudentWelcomeActivity.class));
        }
        else {
            view.getActivity().startActivity(new Intent(view.getActivity(), AdminWelcomeActivity.class));
        }
    }
    public void Failure(Exception e){
        Toast.makeText(view.getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
    }
}

