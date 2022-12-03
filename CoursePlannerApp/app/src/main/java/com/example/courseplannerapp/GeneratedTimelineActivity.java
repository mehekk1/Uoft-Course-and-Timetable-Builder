package com.example.courseplannerapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;

import java.util.ArrayList;
import java.util.HashMap;

public class GeneratedTimelineActivity extends AppCompatActivity {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference coursesRef = database.getReference("CoursesTestVedat");
    DatabaseReference userRef = database.getReference("vedat");
    DatabaseReference takenRef = userRef.child("taken_list");
    DatabaseReference selectedRef = userRef.child("coursesSelected");

    ArrayList<String> selected;
    ArrayList<String> taken;

    Course currentCourse;

    HashMap<String, String> dateTaken;

    String currentSem = "20222";

    boolean stopA = true;
    boolean stopB = true;
    boolean stopC;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generated_timeline);

        takenRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                GenericTypeIndicator<ArrayList<String>> listString = new GenericTypeIndicator<ArrayList<String>>() {};
                taken = task.getResult().getValue(listString);
                if(taken == null) {taken = new ArrayList<String>();}
                for(String code : taken) {
                    dateTaken.put(code, currentSem);
                }

                stopA = false;
            }
        });

        selectedRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                GenericTypeIndicator<ArrayList<String>> listString = new GenericTypeIndicator<ArrayList<String>>() {};
                selected = task.getResult().getValue(listString);
                if(selected == null) {selected = new ArrayList<String>();}

                stopB = false;
            }
        });

        while(stopA || stopB) {/*Waiting for completion of data get from database*/}

        while(selected.size() > 0) {
            stopC = true;
            coursesRef.child(selected.get(selected.size()-1)).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    currentCourse = task.getResult().getValue(Course.class);

                    stopC = false;
                }
            });
            while(stopC){/*Waiting for completion of data get from database*/}

            boolean top = true;

            //Check for untaken prereqs
            for(String prereq : currentCourse.getPrereqs()) {
                if(!taken.contains(prereq)) {
                    top = false;
                    selected.add(prereq);
                }
            }

            if(top) {
                String latest = "";
                for(String prereq : currentCourse.getPrereqs()) {
                    if(latest.compareTo(dateTaken.get(prereq)) < 0) {
                        latest = dateTaken.get(prereq);
                    }
                }

                int year = Integer.parseInt(latest.substring(0,4));

                int earliest;
                if(latest.substring(5).equals("0")) {
                    earliest = 1;
                }
                else if(latest.substring(5).equals("1")) {
                    earliest = 2;
                }
                else {
                    earliest = 0;
                }

                while(!currentCourse.getOfferings().get(earliest)) {
                    earliest++;
                    if(earliest == 3) {
                        year++;
                        earliest = 0;
                    }
                }

                String yearAndSem = Integer.toString(year) + Integer.toString(earliest);
                dateTaken.put(currentCourse.getCode(), yearAndSem);

                selected.remove(currentCourse.getCode());
            }
        }
    }

}