package com.example.courseplannerapp;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EditDialog extends AppCompatDialogFragment {

    HashMap<String, Course> courses;
    String incomingCourseCode;
    Context context;
    FirebaseDatabase mDatabase;
    DatabaseReference mReferenceCourses;
    DatabaseReference mStudentRef;

    public EditDialog(HashMap<String, Course> courses, String incomingCourseCode, Context context){
        this.courses = courses;
        this.incomingCourseCode = incomingCourseCode;
        this.context = context;
        mDatabase = FirebaseDatabase.getInstance();
        mReferenceCourses = mDatabase.getReference("Courses");
        mStudentRef = mDatabase.getReference("Users");
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Confirmation")
                .setMessage("Are you sure you want to delete this course?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        for(Map.Entry<String, Course> entry : courses.entrySet()){
                            if(entry.getValue().getPrereqs() != null && entry.getValue().getPrereqs().contains(incomingCourseCode)){
                                removeCourse(entry.getKey(), incomingCourseCode);
                            }
                            removeUserCourse(incomingCourseCode);
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

    public void removeUserCourse(String deleteCourse) {
        mStudentRef.orderByKey().addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                GenericTypeIndicator<HashMap<String, Object>> userCourses = new GenericTypeIndicator<HashMap<String, Object>>() {};
                HashMap<String, Object> userMap = snapshot.getValue(userCourses);

                List<String> coursesTaken = (List<String>) (userMap.get("taken_list"));
                if(coursesTaken != null && coursesTaken.contains(deleteCourse)){
                    coursesTaken.remove(deleteCourse);
                    mStudentRef.child(snapshot.getKey()).child("taken_list").setValue(coursesTaken);
                }

                List<String> selectedCourses = (List<String>) (userMap.get("coursesSelected"));
                if(selectedCourses != null && selectedCourses.contains(deleteCourse)){
                    selectedCourses.remove(deleteCourse);
                    mStudentRef.child(snapshot.getKey()).child("coursesSelected").setValue(selectedCourses);
                }

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void openAdminEditPage(){
        Intent intent = new Intent(context, AdminEditCoursesActivity.class);
        startActivity(intent);
    }



}
