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
import android.provider.ContactsContract;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;

import java.sql.Array;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class FutureCoursesActivity extends AppCompatActivity {

    private BottomNavigationView bottomNav;

    Context context;
    SharedPreferences sp;
    String user;
    FirebaseDatabase db = FirebaseDatabase.getInstance();
    DatabaseReference selectRef;
    DatabaseReference courseRef = db.getReference("CoursesTestVedat");

    ArrayList<CourseSearchItem> coursesAll;
    ArrayList<String> coursesSelected;
    ArrayList<CourseSearchItem> coursesShown;

    RecyclerView rvSearch;

    Button generate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_future_courses);
        context = this.getApplicationContext();
        sp = getSharedPreferences("save", MODE_PRIVATE);
        user = sp.getString("UID", "defaultUser");
        selectRef = db.getReference("Users/" + user + "/coursesSelected");

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

        coursesAll = new ArrayList<CourseSearchItem>();
        coursesSelected = new ArrayList<String>();

        //Set coursesSelected
        selectRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful()) {
                    GenericTypeIndicator<ArrayList<String>> listType = new GenericTypeIndicator<ArrayList<String>>() {};
                    coursesSelected = task.getResult().getValue(listType);
                    if(coursesSelected == null) {
                        coursesSelected = new ArrayList<String>();
                    }
                    for(String code : coursesSelected) {
                        coursesAll.add(new CourseSearchItem(code, true));
                    }
                }

                initAdded();
                //Get rest of fill out rest of data
                restOfDatabase();
            }
        });

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

        Button generate = findViewById(R.id.future_generate_button);
        generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectRef.setValue(coursesSelected);
                openGeneratedTimelinePage();
            }
        });

        ItemClickSupport.addTo(rvSearch).setOnItemClickListener(
                new ItemClickSupport.OnItemClickListener() {
                    @Override
                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                        searchView.clearFocus();
                        if (position != RecyclerView.NO_POSITION) {
                            CourseSearchItem course = coursesShown.get(position);
                            String code = course.getCode();
                            boolean selected = course.getSelected();

                            if (!selected) {
                                //Change type in all courses
                                //Remove from all courses
                                for(int i = 0; i < coursesAll.size(); i++) {
                                    if(coursesAll.get(i).getCode() == code) {
                                        coursesAll.remove(i);
                                    }
                                }
                                //Re-add to all courses
                                coursesAll.add(coursesSelected.size(), new CourseSearchItem(code, true));

                                //Add to selected list
                                coursesSelected.add(code);
                            }
                            else {
                                //Remove from selected list
                                coursesSelected.remove(code);

                                //Change type in all courses
                                //Remove from all courses
                                for(int i = 0; i < coursesAll.size(); i++) {
                                    if(coursesAll.get(i).getCode() == code) {
                                        coursesAll.remove(i);
                                    }
                                }
                                //Re-add to all courses
                                coursesAll.add(coursesSelected.size(), new CourseSearchItem(code, false));
                            }
                            filterList(((SearchView)findViewById(R.id.future_search_bar)).getQuery().toString());
                            CourseSearchItemAdapter searchAdapter = new CourseSearchItemAdapter(context, coursesShown);
                            rvSearch.setAdapter(searchAdapter);
                            initAdded();
                        }
                    }
                }
        );
    }

    public void restOfDatabase() {
        courseRef.orderByKey().addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Course course = snapshot.getValue(Course.class);
                if(!coursesSelected.contains(course.getCode())) {
                    coursesAll.add(new CourseSearchItem(course.getCode(), false));
                }

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

    private void openGeneratedTimelinePage() {
        Intent intent = new Intent(this, GeneratedTimelineActivity.class);
        startActivity(intent);
    }

}