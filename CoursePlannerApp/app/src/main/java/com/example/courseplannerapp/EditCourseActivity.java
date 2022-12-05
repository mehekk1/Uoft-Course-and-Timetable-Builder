package com.example.courseplannerapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.common.api.internal.zabk;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
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

public class EditCourseActivity extends AppCompatActivity {

    HashMap<String, Course> courses = new HashMap();
    ArrayList<CourseSearchItem> courseSearchItems = new ArrayList<>();
    ArrayList<CourseSearchItem> courseFilter = new ArrayList<>();
    List<String> userId = new ArrayList<>();

    FirebaseDatabase mDatabase;
    DatabaseReference mReferenceCourses;
    DatabaseReference mStudentRef;

    EditText courseEditName, courseEditCode;
    Switch switchFall, switchSummer, swichWinter;
    Button editBtn, removeBtn;
    RecyclerView recyclerView;
    SearchView searchView;
    private BottomNavigationView bottomAdminNav;

    Course course;

    Boolean courseInList;
    Boolean codeInList;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_course);
        context = getApplicationContext();

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        int colorCodeDark = Color.parseColor("#FF000000");
        window.setStatusBarColor(colorCodeDark);

        courseInList = false;
        codeInList = false;

        courseEditName = findViewById(R.id.edit_course_name_txt);
        courseEditCode = findViewById(R.id.edit_course_code_txt);
        switchFall = findViewById(R.id.edit_fall_switch);
        switchSummer = findViewById(R.id.edit_summer_switch);
        swichWinter = findViewById(R.id.edit_winter_switch);
        editBtn = findViewById(R.id.confirm_edit_btn);
        searchView = findViewById(R.id.edit_search_bar);
        removeBtn = findViewById(R.id.confirm_remove_btn);
        bottomAdminNav = findViewById(R.id.bottom_navigation_view);
        bottomAdminNav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch (id) {
                    case R.id.nav_home:
                        openWelcomePage();
                        break;
                    case R.id.nav_timeline:
                        openAdminAddCoursesPage();
                        break;
                    case R.id.nav_add_menu:
                        openAdminEditPage();
                        break;
                }
                return true;
            }
        });


        Intent incomingIntent = getIntent();
        String incomingCourseCode = incomingIntent.getStringExtra("courseCode");

        mDatabase = FirebaseDatabase.getInstance();
        mReferenceCourses = mDatabase.getReference("Courses");
        mStudentRef = mDatabase.getReference("Users");

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
                    openAdminEditPage();

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

                        for(Map.Entry<String, Course> entry : courses.entrySet()){
                            if(entry.getValue().getPrereqs() != null && entry.getValue().getPrereqs().contains(incomingCourseCode)){
                                editCoursePreReq(entry.getKey(), incomingCourseCode, newCourseCode);
                            }
                        }


                    }
                }

            }
        });

        removeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog(courses, incomingCourseCode);
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


                    for(int i = 0; i < checker.getPrereqs().size(); i++){
                        if(checker.getPrereqs().get(i) != null && checker.getPrereqs().get(i).equals(editCourse)){
                            checker.getPrereqs().set(i, newCourse);
                            mReferenceCourses.child(checkCourse).child("prereqs").setValue(checker.getPrereqs());
                            openAdminEditPage();
                            break;
                        }
                    }
            }
        });

        

        mStudentRef.orderByKey().addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                GenericTypeIndicator<HashMap<String, Object>> userCourses = new GenericTypeIndicator<HashMap<String, Object>>() {};
                HashMap<String, Object> userMap = snapshot.getValue(userCourses);

                List<String> coursesTaken = (List<String>) (userMap.get("taken_list"));
                if(coursesTaken != null && coursesTaken.contains(editCourse)){
                    coursesTaken.remove(editCourse);
                    coursesTaken.add(newCourse);
                    mStudentRef.child(snapshot.getKey()).child("taken_list").setValue(coursesTaken);
                }

                List<String> selectedCourses = (List<String>) (userMap.get("coursesSelected"));
                if(selectedCourses != null && selectedCourses.contains(editCourse)){
                    selectedCourses.remove(editCourse);
                    selectedCourses.add(newCourse);
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
        Intent intent = new Intent(this, AdminEditCoursesActivity.class);
        startActivity(intent);
    }

    public void openDialog(HashMap<String, Course> courses, String deleteCourse){
        EditDialog exDialog = new EditDialog(courses, deleteCourse, context);
        exDialog.show(getSupportFragmentManager(), "ex Dialog");
    }

    private void openWelcomePage(){
        Intent intent = new Intent(this, AdminWelcomeActivity.class);
        startActivity(intent);
    }

    private void openAdminAddCoursesPage(){
        Intent intent = new Intent(this, AdminAddCourseActivity.class);
        startActivity(intent);
    }

}
