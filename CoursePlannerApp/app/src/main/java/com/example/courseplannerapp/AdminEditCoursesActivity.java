package com.example.courseplannerapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AdminEditCoursesActivity extends AppCompatActivity {

    ArrayList<Course> editableCourses = new ArrayList<>();
    FirebaseDatabase mDatabase;
    DatabaseReference mReferenceCourses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_edit_courses);

    }

    public interface dataStatus{
        void DataIsLoaded(List<Course> courses, List<String> keys);
        void DataIsInserted();
        void DataIsUpdated();
        void DataIsdeleted();
    }

    private void setUpCourses(final dataStatus data){
        mDatabase = FirebaseDatabase.getInstance();
        mReferenceCourses = mDatabase.getReference("Courses");

        mReferenceCourses.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
//              Every time some update has happened onDataChange method is activated

                editableCourses.clear();
                List<String> keys = new ArrayList<>();
                for(DataSnapshot keyNode : snapshot.getChildren()){
                    keys.add(keyNode.getKey());
                    Course course = keyNode.getValue(Course.class);
                    editableCourses.add(course);
                }
                data.DataIsLoaded(editableCourses, keys);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}