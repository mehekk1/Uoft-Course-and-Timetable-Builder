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
    Presenter presenter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.loginpage_fragment, container, false);

        loginbtn = root.findViewById(R.id.login);
        Email = root.findViewById(R.id.username);
        Pass = root.findViewById(R.id.password);
        presenter = new Presenter(getActivity(), new Model(presenter));

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateInfo();
            }
        });
        return root;
    }

    public void validateInfo(){

        email = Email.getText().toString();
        pass = Pass.getText().toString();

        if(presenter.ButtonError(Email, email)){ // condition if full name field is empty,
            return;
        }
        if(presenter.ButtonError(Pass, pass)){ // condition if full name field is empty,
            return;
        }
        presenter.Login(email, pass);
    }
}
