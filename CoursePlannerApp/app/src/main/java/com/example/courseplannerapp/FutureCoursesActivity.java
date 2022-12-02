package com.example.courseplannerapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.sql.Array;
import java.util.ArrayList;

public class FutureCoursesActivity extends AppCompatActivity {

    private BottomNavigationView bottomNav;

    FirebaseDatabase db = FirebaseDatabase.getInstance();;
    DatabaseReference selectRef = db.getReference("vedat/coursesSelected");
    DatabaseReference courseRef = db.getReference("CoursesTestVedat");
    ArrayList<CourseSearchItem> coursesAll;
    ArrayList<String> coursesSelected;

    ArrayList<CourseSearchItem> coursesShown;

    Context context;

    RecyclerView rvSearch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_future_courses);

        bottomNav = findViewById(R.id.bottom_navigation_view);
        bottomNav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch (id) {
                    case R.id.nav_home:
                        openWelcomePage();
                        break;
                    case R.id.nav_timeline:
                        break;
                    case R.id.nav_add_menu:
                        openTakenTimelinePage();
                        break;
                }
                return true;
            }
        });


        context = this.getApplicationContext();
        coursesAll = new ArrayList<CourseSearchItem>();
        coursesSelected = new ArrayList<String>();

        rvSearch = findViewById(R.id.search_items_view);
        CourseSearchItemAdapter searchAdapter = new CourseSearchItemAdapter(context, coursesAll);
        rvSearch.setAdapter(searchAdapter);
        rvSearch.setLayoutManager(new LinearLayoutManager(this));

        filterList("");

        SearchView searchView = findViewById(R.id.future_search_bar);
        searchView.clearFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText);
                return true;
            }
        });

        initAdded();

        ItemClickSupport.addTo(rvSearch).setOnItemClickListener(
                new ItemClickSupport.OnItemClickListener() {
                    @Override
                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                        searchView.clearFocus();
                        if (position != RecyclerView.NO_POSITION) {
                            CourseSearchItem course = coursesShown.get(position);
                            String courseCode;
                            courseCode = course.getCode();

                            if (course.getSelected() == false) {
                                //Add to selected data
                                selectRef.child(courseCode).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                                        if (task.isSuccessful()) {
                                            selectRef.child(courseCode).setValue(courseCode);
                                        } else {
                                            Toast.makeText(context, "An error has occurred", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });

//                                //Remove from coursesList
//                                for(int i = 0; i < coursesAll.size(); i++) {
//                                    CourseSearchItem currentCourse = coursesAll.get(i);
//                                    if(currentCourse.getCode().equals(courseCode)) {
//                                        coursesAll.remove(i);
//                                        break;
//                                    }
//                                }
//                                //Re-add to coursesAll end of selected as selected
//                                coursesAll.add(coursesSelected.size(), new CourseSearchItem(course.getCode(), true));
//                                filterList(((SearchView)findViewById(R.id.future_search_bar)).getQuery().toString());
//                                CourseSearchItemAdapter searchAdapter = new CourseSearchItemAdapter(context, coursesShown);
//                                rvSearch.setAdapter(searchAdapter);
                            }
                            else {
                                //Remove from selected data
                                selectRef.child(courseCode).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                                        if (task.isSuccessful()) {
                                            selectRef.child(courseCode).removeValue();
                                        } else {
                                            Toast.makeText(context, "An error has occurred", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        }
                    }
                }
        );

        courseRef.orderByKey().addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Course course = snapshot.getValue(Course.class);
                coursesAll.add(coursesSelected.size(), new CourseSearchItem(course.getCode(), false));

                filterList(((SearchView)findViewById(R.id.future_search_bar)).getQuery().toString());
                CourseSearchItemAdapter searchAdapter = new CourseSearchItemAdapter(context, coursesShown);
                rvSearch.setAdapter(searchAdapter);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                Course course = snapshot.getValue(Course.class);
                for(int i = 0; i < coursesAll.size(); i++) {
                    CourseSearchItem currentCourse = coursesAll.get(i);
                    if(currentCourse.getCode().equals(course.getCode())) {
                        coursesAll.remove(i);
                        break;
                    }
                }

                filterList(((SearchView)findViewById(R.id.future_search_bar)).getQuery().toString());
                CourseSearchItemAdapter searchAdapter = new CourseSearchItemAdapter(context, coursesShown);
                rvSearch.setAdapter(searchAdapter);

                coursesSelected.remove(snapshot.getValue().toString());
                initAdded();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

        selectRef.orderByKey().addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                //Remove from coursesList
                for(int i = 0; i < coursesAll.size(); i++) {
                    CourseSearchItem currentCourse = coursesAll.get(i);
                    if(currentCourse.getCode().equals(snapshot.getValue().toString())) {
                        coursesAll.remove(i);
                        break;
                    }
                }
                //Re-add to coursesAll end of selected as selected
                coursesAll.add(coursesSelected.size(), new CourseSearchItem(snapshot.getValue().toString(), true));

                filterList(((SearchView)findViewById(R.id.future_search_bar)).getQuery().toString());
                CourseSearchItemAdapter searchAdapter = new CourseSearchItemAdapter(context, coursesShown);
                rvSearch.setAdapter(searchAdapter);

                coursesSelected.add(snapshot.getValue().toString());
                initAdded();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                //Remove from coursesList
                for(int i = 0; i < coursesAll.size(); i++) {
                    CourseSearchItem currentCourse = coursesAll.get(i);
                    if(currentCourse.getCode().equals(snapshot.getValue().toString())) {
                        coursesAll.remove(i);
                        break;
                    }
                }
                //Re-add to coursesAll end of selected as not selected
                coursesAll.add(coursesSelected.size()-1, new CourseSearchItem(snapshot.getValue().toString(), false));

                filterList(((SearchView)findViewById(R.id.future_search_bar)).getQuery().toString());
                CourseSearchItemAdapter searchAdapter = new CourseSearchItemAdapter(context, coursesShown);
                rvSearch.setAdapter(searchAdapter);

                coursesSelected.remove(snapshot.getValue().toString());
                initAdded();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void initAdded(){
        TableLayout act = findViewById(R.id.added_courses_table);
        int count = act.getChildCount();
        for (int i = 0; i < count; i++) {
            View child = act.getChildAt(i);
            if (child instanceof TableRow) ((ViewGroup) child).removeAllViews();
        }

        for(String c:coursesSelected){
            TableRow tbrow = new TableRow(context);
            TextView tv1 = new TextView(context);
            tv1.setText(c);
            tv1.setTextSize(18);
            tbrow.addView(tv1);
            act.addView(tbrow);
        }
    }
    
    public void filterList(String text) {
        coursesShown = new ArrayList<CourseSearchItem>();
        for(CourseSearchItem course : coursesAll) {
            if(course.getCode().toLowerCase().contains(text.toLowerCase())) {
                coursesShown.add(course);
            }
        }

        CourseSearchItemAdapter searchAdapter = new CourseSearchItemAdapter(context, coursesShown);
        rvSearch.setAdapter(searchAdapter);
    }

    private void openWelcomePage () {
        Intent intent = new Intent(this, StudentWelcomeActivity.class);
        startActivity(intent);
    }

    private void openTakenTimelinePage () {
        Intent intent = new Intent(this, TakenTimelineActivity.class);
        startActivity(intent);
    }

}