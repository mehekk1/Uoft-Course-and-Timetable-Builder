package com.example.courseplannerapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

import java.util.ArrayList;
import java.util.List;

public class SignUpTabFragment extends Fragment {

    EditText Email, Pass, ConfirmPass;
    Button Registerbtn;
    RadioButton student, admin;
    boolean isStudent;
    String email, password, confirm;
    FirebaseAuth fAuth;
    FirebaseDatabase database;
    DatabaseReference mDatabase;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.signuppage_fragment, container, false);

        Email = root.findViewById(R.id.username2);
        Pass = root.findViewById(R.id.password2);
        ConfirmPass = root.findViewById(R.id.password3);
        Registerbtn = root.findViewById(R.id.login_button);
        student = root.findViewById(R.id.radioButtonStudent);
        admin = root.findViewById(R.id.radioButtonAdmin);
        database = FirebaseDatabase.getInstance();
        mDatabase = database.getReference("Users");
        fAuth = FirebaseAuth.getInstance();


        Registerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = Email.getText().toString();
                password = Pass.getText().toString();
                confirm = ConfirmPass.getText().toString();


                if(email.isEmpty()){ // condition if full name field is empty,
                    Email.setError("This field cannot be empty."); // this error message will be shown.
                    return;
                }

                if(password.isEmpty()){ // condition if full name field is empty,
                    Pass.setError("This field cannot be empty."); // this error message will be shown.
                    return;
                }

                if(confirm.isEmpty()){ // condition if full name field is empty,
                    ConfirmPass.setError("This field cannot be empty."); // this error message will be shown.
                    return;
                }

                if (!confirm.equals(password)){ // condition if the confirm password is not equals to password,
                    ConfirmPass.setError("Password does not match."); // this error message will be shown.
                    return;
                }

                //Register User in firebase

                Toast.makeText(getActivity(),"Data Validated",Toast.LENGTH_LONG).show();

                fAuth.createUserWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {

                    @Override
                    public void onSuccess(AuthResult authResult) { // send the user to the log - in screen.

                        if (student.isChecked()){
                            isStudent = true;
                        }
                        else {
                            isStudent = false;
                        }

                        User user = new User(isStudent, new ArrayList<String>());
                        String keyid = fAuth.getInstance().getCurrentUser().getUid();
                        mDatabase.child(keyid).setValue(user);

                        if (isStudent){
                            startActivity(new Intent(getActivity(), StudentWelcomeActivity.class));
                        }
                        else {
                            startActivity(new Intent(getActivity(), AdminWelcomeActivity.class));
                        }
                    }

                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) { // an error when email and pass is wrong or invalid.

                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        return root;
    }
}
