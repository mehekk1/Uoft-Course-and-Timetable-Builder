package com.example.courseplannerapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import java.util.ArrayList;

public class Model {

    Presenter presenter;
    FirebaseAuth fAuth;
    FirebaseDatabase database;
    DatabaseReference mDatabase;
    String currentUser;
    boolean isStudent;

    public Model(Presenter presenter){
        this.presenter = presenter;
    }

    public void Login(String email, String pass) {

        database = FirebaseDatabase.getInstance();
        mDatabase = database.getReference("Users");
        fAuth = FirebaseAuth.getInstance();


        fAuth.signInWithEmailAndPassword(email, pass)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {

                    @Override
                    public void onSuccess(AuthResult authResult) {
                        currentUser = fAuth.getInstance().getCurrentUser().getUid();

                        mDatabase.child(currentUser).child("student").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DataSnapshot> task) {
                                isStudent = task.getResult().getValue(Boolean.class);
                                presenter.Success(currentUser, isStudent);
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        presenter.Failure(e);
                    }
                });
    }
}
