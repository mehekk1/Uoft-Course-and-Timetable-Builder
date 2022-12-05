package com.example.courseplannerapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.EditText;
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

public class Presenter implements PresenterListener {

    FirebaseAuth fAuth;
    FirebaseDatabase database;
    DatabaseReference mDatabase;
    SharedPreferences sp;

    @Override
    public void Login(Activity activity, String email, String pass) {

        database = FirebaseDatabase.getInstance();
        mDatabase = database.getReference("Users");
        sp = activity.getSharedPreferences("save", Context.MODE_PRIVATE);
        fAuth = FirebaseAuth.getInstance();
        SharedPreferences.Editor editor = sp.edit();


        fAuth.signInWithEmailAndPassword(email, pass)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {

            @Override
            public void onSuccess(AuthResult authResult) {
                String currentuser = fAuth.getInstance().getCurrentUser().getUid();

                mDatabase.child(currentuser).child("student").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {

                        editor.putString("UID",currentuser);
                        editor.apply();

                        boolean isStudent = task.getResult().getValue(Boolean.class);
                        if (isStudent){
                            activity.startActivity(new Intent(activity, StudentWelcomeActivity.class));
                        }
                        else {
                            activity.startActivity(new Intent(activity, AdminWelcomeActivity.class));
                        }
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(activity, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}

