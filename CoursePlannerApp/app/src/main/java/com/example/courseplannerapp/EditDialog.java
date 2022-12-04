package com.example.courseplannerapp;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EditDialog extends AppCompatDialogFragment {

    HashMap<String, Course> courses;
    String incomingCourseCode;
    Context context;
    FirebaseDatabase mDatabase;
    DatabaseReference mReferenceCourses;

    public EditDialog(HashMap<String, Course> courses, String incomingCourseCode, Context context){
        this.courses = courses;
        this.incomingCourseCode = incomingCourseCode;
        this.context = context;
        mDatabase = FirebaseDatabase.getInstance();
        mReferenceCourses = mDatabase.getReference("CoursesTestVedat");
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Confirmation")
                .setMessage("Dialog")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        for(Map.Entry<String, Course> entry : courses.entrySet()){
                            if(entry.getValue().getPrereqs() != null && entry.getValue().getPrereqs().contains(incomingCourseCode)){
                                removeCourse(entry.getKey(), incomingCourseCode);
                            }
                        }
                        mReferenceCourses.child(incomingCourseCode).removeValue();
                        openAdminEditPage();
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });

        return builder.create();
    }


    public void removeCourse(String removeCourse, String removeCoursePreReq){

        mReferenceCourses.child(removeCourse).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                Course checker;
                DataSnapshot dataSnapshot = task.getResult();
                checker = dataSnapshot.getValue(Course.class);
                ArrayList<String> newPrereqs = new ArrayList<>();

                if(checker.getPrereqs() != null){
                    for(int i = 0; i < checker.getPrereqs().size(); i++){
                        if(checker.getPrereqs() != null && !(checker.getPrereqs().get(i).equals(removeCoursePreReq))){
                            newPrereqs.add(checker.getPrereqs().get(i));
                        }
                    }

                    mReferenceCourses.child(removeCourse).child("prereqs").setValue(newPrereqs);
                    mReferenceCourses.child(removeCoursePreReq).removeValue();
                }


            }
        });

    }

    public void openAdminEditPage(){
        Intent intent = new Intent(context, AdminEditCoursesActivity.class);
        startActivity(intent);
    }



}
