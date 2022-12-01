package com.example.courseplannerapp;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AdminEditFirebaseHelper {

    private FirebaseDatabase myDatabase;
    private DatabaseReference myReferenceCourses;
    private List<Course> courses = new ArrayList<>();

    public interface DataStatus{
        void DataIsLoaded(List<Course> courses, List<String> keys);
        void DataIsInserted();
        void DataIsUpdated();
        void DataIsDeleted();
    }

    public AdminEditFirebaseHelper(){
        myDatabase = FirebaseDatabase.getInstance();
        myReferenceCourses = myDatabase.getReference("CoursesTestVedat");
    }

    public void readCourses(final DataStatus dataStatus){
        myReferenceCourses.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                courses.clear();
                List<String> keys = new ArrayList<>();
                for(DataSnapshot keyNode : snapshot.getChildren()){
                    keys.add(keyNode.getKey());
                    Course course = keyNode.getValue(Course.class);
                    courses.add(course);
                }
                dataStatus.DataIsLoaded(courses, keys);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}
