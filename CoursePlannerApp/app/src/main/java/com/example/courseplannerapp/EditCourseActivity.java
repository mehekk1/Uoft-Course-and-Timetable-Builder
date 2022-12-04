package com.example.courseplannerapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Switch;
import android.widget.Toast;

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

public class EditCourseActivity extends AppCompatActivity {

    HashMap<String, Course> courses = new HashMap();
    ArrayList<CourseSearchItem> courseSearchItems = new ArrayList<>();
    ArrayList<CourseSearchItem> courseFilter = new ArrayList<>();

    FirebaseDatabase mDatabase;
    DatabaseReference mReferenceCourses;

    EditText courseEditName, courseEditCode;
    Switch switchFall, switchSummer, swichWinter;
    Button editBtn;
    RecyclerView recyclerView;
    SearchView searchView;

    Course course;

    Boolean courseInList;
    Boolean codeInList;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_course);
        context = getApplicationContext();

        courseInList = false;
        codeInList = false;

        courseEditName = findViewById(R.id.edit_course_name_txt);
        courseEditCode = findViewById(R.id.edit_course_code_txt);
        switchFall = findViewById(R.id.edit_fall_switch);
        switchSummer = findViewById(R.id.edit_summer_switch);
        swichWinter = findViewById(R.id.edit_winter_switch);
        editBtn = findViewById(R.id.confirm_edit_btn);
        searchView = findViewById(R.id.edit_search_bar);


        Intent incomingIntent = getIntent();
        String incomingCourseCode = incomingIntent.getStringExtra("courseCode");

        mDatabase = FirebaseDatabase.getInstance();
        mReferenceCourses = mDatabase.getReference("CoursesTestVedat");

//        Chain aysnc for loading course data
        mReferenceCourses.child(incomingCourseCode).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {

                DataSnapshot dataSnapshot = task.getResult();
                course = dataSnapshot.getValue(Course.class);

                courseEditName.setText(course.name);
                courseEditCode.setText(course.code);

                swichWinter.setChecked(course.offerings.get(0));
                switchSummer.setChecked(course.offerings.get(1));
                switchFall.setChecked(course.offerings.get(2));

                createCoursesList();
            }
        });
//      Clicking on the
        CourseSearchItemAdapter searchItemAdapter = new CourseSearchItemAdapter(context, courseFilter);
        recyclerView = findViewById(R.id.edit_recycle_search);
        recyclerView.setAdapter(searchItemAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        filterList("");


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                filterList(s);
                return true;
            }
        });


        ItemClickSupport.addTo(recyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                if(position != RecyclerView.NO_POSITION){

                    String clickedCourseCode = courseFilter.get(position).getCode();
                    Boolean selected = courseFilter.get(position).getSelected();
                    if(!selected){
                        for(int i = 0; i < courseSearchItems.size(); i++){
                            if(clickedCourseCode.equals(courseSearchItems.get(i).getCode())){
                                courseSearchItems.remove(i);
                            }
                        }
                        courseSearchItems.add(0, new CourseSearchItem(clickedCourseCode, true));
                    }
                    else{
                        for(int i = 0; i < courseSearchItems.size(); i++){
                            if(clickedCourseCode.equals(courseSearchItems.get(i).getCode())){
                                courseSearchItems.remove(i);
                            }
                        }
                        courseSearchItems.add(new CourseSearchItem(clickedCourseCode, false));
                    }
                    filterList(searchView.getQuery().toString());
                    CourseSearchItemAdapter searchItemAdapter = new CourseSearchItemAdapter(context, courseFilter);
                    recyclerView.setAdapter(searchItemAdapter);
                }
            }
        });

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!(swichWinter.isChecked() || switchFall.isChecked() || switchSummer.isChecked())){
                    Toast.makeText(context, "Must have atleast one offering", Toast.LENGTH_SHORT).show();
                }
                String newCourseName = courseEditName.getText().toString();
                String newCourseCode = courseEditCode.getText().toString();

                if(incomingCourseCode.equals(newCourseCode)){

                    mReferenceCourses.child(incomingCourseCode).child("name").setValue(newCourseName);

                    ArrayList<Boolean> offerings = new ArrayList<>();
                    offerings.add(swichWinter.isChecked());
                    offerings.add(switchSummer.isChecked());
                    offerings.add(switchFall.isChecked());
                    mReferenceCourses.child(incomingCourseCode).child("offerings").setValue(offerings);

                    ArrayList<String> preReqs = new ArrayList<>();
                    int i = 0;
                    while(courseSearchItems.get(i).getSelected()){
                        preReqs.add(courseSearchItems.get(i).getCode());
                        i++;
                    }
                    mReferenceCourses.child(incomingCourseCode).child("prereqs").setValue(preReqs);

                }
                else{
                    if(courses.keySet().contains(newCourseCode)){
                        Toast.makeText(context, "Course code name already exists", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Course newCourse = new Course();
                        newCourse.setName(courseEditName.getText().toString());
                        newCourse.setCode(newCourseCode);

                        ArrayList<Boolean> offerings = new ArrayList<>();
                        offerings.add(swichWinter.isChecked());
                        offerings.add(switchSummer.isChecked());
                        offerings.add(switchFall.isChecked());
                        newCourse.setOfferings(offerings);

                        ArrayList<String> preReqs = new ArrayList<>();
                        int i = 0;
                        while(courseSearchItems.get(i).getSelected()){
                            preReqs.add(courseSearchItems.get(i).getCode());
                            i++;
                        }
                        newCourse.setPrereqs(preReqs);

                        mReferenceCourses.child(newCourse.getCode()).setValue(newCourse);
                        mReferenceCourses.child(incomingCourseCode).removeValue();

                        for(String currCourse : courses.keySet()){
                            if(currCourse.contains(incomingCourseCode)){
                                editCoursePreReq(currCourse, incomingCourseCode, newCourseCode);
                            }
                        }


                    }
                }

            }
        });

    }

    public void createCoursesList(){
        mReferenceCourses.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                GenericTypeIndicator<HashMap<String, Course>> coursesMap = new GenericTypeIndicator<HashMap<String, Course>>() {};
                courses = task.getResult().getValue(coursesMap);
                createList();
            }
        });
    }

    public void createList(){

        List<String> preReqs = courses.get(course.getCode()).getPrereqs();
        if(preReqs == null){
            preReqs = new ArrayList<>();
        }
        for(String currCourse : courses.keySet()){
            if(preReqs.contains(currCourse)){
                courseSearchItems.add(0, new CourseSearchItem(currCourse, true));
            }
            else{
                courseSearchItems.add(new CourseSearchItem(currCourse, false));
            }
        }

        filterList(searchView.getQuery().toString());
        CourseSearchItemAdapter searchItemAdapter = new CourseSearchItemAdapter(context, courseFilter);
        recyclerView.setAdapter(searchItemAdapter);

    }


    public void filterList(String text){
        courseFilter = new ArrayList<CourseSearchItem>();
        for (CourseSearchItem course: courseSearchItems){
            if (course.getCode().toUpperCase().contains(text.toUpperCase())){
                courseFilter.add(course);
            }
        }

        CourseSearchItemAdapter searchAdapter = new CourseSearchItemAdapter(context, courseFilter);
        recyclerView.setAdapter((searchAdapter));
    }

    public void editCoursePreReq(String checkCourse, String editCourse, String newCourse){

        mReferenceCourses.child(checkCourse).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                Course checker;
                DataSnapshot dataSnapshot = task.getResult();
                checker = dataSnapshot.getValue(Course.class);
                ArrayList<String> newPreReqs = new ArrayList<>();
                newPreReqs = (ArrayList<String>) checker.getPrereqs();

                for(int i = 0; i < newPreReqs.size(); i++){
                    if(checker.getPrereqs().get(i).equals(editCourse)){
                        checker.getPrereqs().set(i, newCourse);
                        mReferenceCourses.child(checkCourse).child("prereqs").setValue(checker.getPrereqs());
                        break;
                    }
                }

            }
        });

    }

}
