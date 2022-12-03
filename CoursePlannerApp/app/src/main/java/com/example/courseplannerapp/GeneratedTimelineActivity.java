package com.example.courseplannerapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GeneratedTimelineActivity extends AppCompatActivity {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference coursesRef = database.getReference("CoursesTestVedat");
    DatabaseReference userRef = database.getReference("vedat");
    DatabaseReference takenRef = userRef.child("taken_list");
    DatabaseReference selectedRef = userRef.child("coursesSelected");
    Context context;

    ArrayList<String> selected = new ArrayList<String>();
    ArrayList<String> taken = new ArrayList<String>();

    Course currentCourse;

    HashMap<String, String> dateAlreadyTaken = new HashMap<String, String>();
    HashMap<String, String> dateNewTaken = new HashMap<String, String>();
    HashMap<String, Course> allCourses = new HashMap<String, Course>();

    String startSem = "20222";

    RecyclerView display;

    boolean A = false;
    boolean B = false;
    boolean C = false;

    Button backToSelectCourses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generated_timeline);
        context = this.getApplicationContext();

        takenRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                GenericTypeIndicator<ArrayList<String>> listString = new GenericTypeIndicator<ArrayList<String>>() {};
                taken = task.getResult().getValue(listString);
                if(taken == null) {taken = new ArrayList<String>();}
                for(String code : taken) {
                    dateAlreadyTaken.put(code, startSem);
                }

                A = true;
                runCode();
            }
        });

        selectedRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                GenericTypeIndicator<ArrayList<String>> listString = new GenericTypeIndicator<ArrayList<String>>() {};
                selected = task.getResult().getValue(listString);
                if(selected == null) {selected = new ArrayList<String>();}

                B = true;
                runCode();
            }
        });

        coursesRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                GenericTypeIndicator<HashMap<String, Course>> hashGeneric = new GenericTypeIndicator<HashMap<String, Course>>() {};
                allCourses = task.getResult().getValue(hashGeneric);
                C = true;
                runCode();
            }
        });

        backToSelectCourses = findViewById(R.id.addMoreCourses);
        backToSelectCourses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFutureCoursesPage();
            }
        });
    }

    public void runCode() {
        if(A && B && C) {
            while(selected.size() > 0) {
                boolean top = true;

                currentCourse = allCourses.get(selected.get(selected.size()-1));
                List<String> preReqs = currentCourse.getPrereqs();
                if(preReqs == null) {preReqs = new ArrayList<String>();}

                //Check for untaken prereqs
                for(String prereq : preReqs) {
                    if(!taken.contains(prereq)) {
                        top = false;
                        selected.add(prereq);
                    }
                }

                if(top) {
                    String latest = startSem;
                    for(String prereq : preReqs) {
                        if(dateAlreadyTaken.get(prereq) != null) {
                            if(latest.compareTo(dateAlreadyTaken.get(prereq)) < 0) {
                                latest = dateAlreadyTaken.get(prereq);
                            }
                        }
                        else {
                            if(latest.compareTo(dateNewTaken.get(prereq)) < 0) {
                                latest = dateNewTaken.get(prereq);
                            }
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
                        year++;
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
                    dateNewTaken.put(currentCourse.getCode(), yearAndSem);
                    taken.add(currentCourse.getCode());

                    selected.remove(currentCourse.getCode());
                }
            }

            //CONVERT VALUES TO LIST OF STRING AND LIST OF LIST
            //Add years in chronological order
            ArrayList<String> yearAndSem = new ArrayList<String>();

            for(String yearSem : dateNewTaken.values()) {
                if(!yearAndSem.contains(yearSem)) {
                    boolean added = false;
                    for(int i = 0; i < yearAndSem.size(); i++) {
                        if(yearSem.compareTo(yearAndSem.get(i)) < 0) {
                            yearAndSem.add(i, yearSem);
                            added = true;
                            break;
                        }
                    }
                    if(!added) {
                        yearAndSem.add(yearSem);
                    }
                }
            }
            //Add courses to take list in chronological order
            ArrayList<ArrayList<String>> coursesToTake = new ArrayList<ArrayList<String>>();

            for(int i = 0; i < yearAndSem.size(); i++) {
                coursesToTake.add(new ArrayList<String>());
            }

            for(String course : dateNewTaken.keySet()) {
                coursesToTake.get(yearAndSem.indexOf(dateNewTaken.get(course))).add(course);
            }

            //ADD VALUES TO DISPLAY
            display = findViewById(R.id.generateMainRecycler);
            GenerateTimelineAdapter mainRecycleAdapter = new GenerateTimelineAdapter(context, yearAndSem, coursesToTake);
            display.setAdapter(mainRecycleAdapter);
            display.setLayoutManager(new LinearLayoutManager(context));
        }
    }

    public void openFutureCoursesPage() {
        Intent intent = new Intent(this, FutureCoursesActivity.class);
        startActivity(intent);
    }
}