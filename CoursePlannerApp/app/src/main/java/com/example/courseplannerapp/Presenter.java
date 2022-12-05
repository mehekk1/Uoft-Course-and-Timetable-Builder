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
    Activity activity;
    Model model;
    public Presenter(Activity activity, Model model){
        this.activity = activity;
        this.model = model;
    }

    public void Login(String email, String password){
        model.Login(email,password);
    }
    public void addUserSharedPref(String CurrentUser){

        SharedPreferences sp;
        sp = activity.getSharedPreferences("save", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("UID",CurrentUser);
        editor.apply();

    }
    public void Success(boolean isStudent){
        if (isStudent){
            activity.startActivity(new Intent(activity, StudentWelcomeActivity.class));
        }
        else {
            activity.startActivity(new Intent(activity, AdminWelcomeActivity.class));
        }
    }
    public void Failure(Exception e){
        Toast.makeText(activity, e.getMessage(), Toast.LENGTH_LONG).show();
    }
    public boolean ButtonError(EditText editText, String text){

        if(text.isEmpty()){ // condition if full name field is empty,
            editText.setError("This field cannot be empty."); // this error message will be shown.
            return true;
        }
        return false;
    }




}

