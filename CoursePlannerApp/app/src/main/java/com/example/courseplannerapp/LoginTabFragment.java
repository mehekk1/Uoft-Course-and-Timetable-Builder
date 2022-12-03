package com.example.courseplannerapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginTabFragment extends Fragment {

    Button loginbtn;
    EditText Email, Pass;
    String email, pass;
    FirebaseAuth fAuth;
    FirebaseDatabase database;
    DatabaseReference mDatabase;
    SharedPreferences sp;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.loginpage_fragment, container, false);

        fAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        mDatabase = database.getReference("Users");
        Email = root.findViewById(R.id.username);
        Pass = root.findViewById(R.id.password);
        loginbtn = root.findViewById(R.id.login);
        sp = getActivity().getSharedPreferences("save", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();


        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = Email.getText().toString();
                pass = Pass.getText().toString();

                if(email.isEmpty()){ // condition if full name field is empty,
                    Email.setError("This field cannot be empty."); // this error message will be shown.
                    return;
                }
                if(pass.isEmpty()){ // condition if full name field is empty,
                    Pass.setError("This field cannot be empty."); // this error message will be shown.
                    return;
                }

                fAuth.signInWithEmailAndPassword(email, pass).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
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
                                    startActivity(new Intent(getActivity(), StudentWelcomeActivity.class));
                                }
                                else {
                                    startActivity(new Intent(getActivity(), AdminWelcomeActivity.class));
                                }
                            }
                        });
                        //startActivity(new Intent(getActivity(), LoginActivity.class));
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        return root;
    }
}
