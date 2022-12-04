package com.example.courseplannerapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.example.courseplannerapp.databinding.ActivityAdminAddCourseBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AdminAddCourseActivity extends AppCompatActivity {
    ActivityAdminAddCourseBinding binding;
    String code, name;
    //boolean fall, winter, summer;
    ArrayList<Boolean> offerings;
    ArrayList<String> prereqs;
    FirebaseDatabase database;
    DatabaseReference ref;
    ArrayList<CourseSearchItem> prereq_search;
    RecyclerView rvs;
    Context context;
    ArrayList<CourseSearchItem> courseFilter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminAddCourseBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        context = this.getApplicationContext();
        rvs = binding.searchRecycle;
        database = FirebaseDatabase.getInstance();
        prereq_search = new ArrayList<CourseSearchItem>();
        CourseSearchItemAdapter searchAdapter = new CourseSearchItemAdapter(context, prereq_search);
        rvs.setAdapter((searchAdapter));
        rvs.setLayoutManager(new LinearLayoutManager(this));

        filterList("");
        SearchView sv = binding.searchView;
        sv.clearFocus();
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText);
                return true;
            }
        });



        ItemClickSupport.addTo(rvs).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                if (position != RecyclerView.NO_POSITION){
                    CourseSearchItem csi = courseFilter.get(position);
                    String courseCode = csi.getCode();
                    if (csi.getSelected() == false){
                        for (int i = 0; i < prereq_search.size(); i++) {
                            CourseSearchItem currentCourse = prereq_search.get(i);
                            if (currentCourse.getCode().equals(courseCode)){
                                prereq_search.remove(i);
                                break;
                            }
                        }
                        prereq_search.add(0, new CourseSearchItem(courseCode, true));
                    }
                    else{
                        for (int i = 0; i < prereq_search.size(); i++) {
                            CourseSearchItem currentCourse = prereq_search.get(i);
                            if (currentCourse.getCode().equals(courseCode)){
                                prereq_search.remove(i);
                                break;
                            }
                        }
                        prereq_search.add(prereq_search.size(), new CourseSearchItem(courseCode, false));
                    }
                    filterList(binding.searchView.getQuery().toString());
                    CourseSearchItemAdapter searchAdapter = new CourseSearchItemAdapter(context, courseFilter);
                    rvs.setAdapter((searchAdapter));
                }


            }
        });



        binding.adminAddCourseBtn.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                code = binding.adminAddCourseCode.getText().toString().toUpperCase();
                name = binding.adminAddCourseName.getText().toString();
                offerings = new ArrayList<Boolean>();
                offerings.add(0, binding.winterSwitch.isChecked());
                offerings.add(1, binding.summerSwitch.isChecked());
                offerings.add(2, binding.fallSwitch.isChecked());
                prereqs = new ArrayList<String>();
                int i = 0;
                while (prereq_search.get(i).getSelected()){
                    prereqs.add(prereq_search.get(i).getCode());
                    i++;
                }
                ref = database.getReference("Courses");
                ref.child(code).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (task.isSuccessful()){
                            if (code.isEmpty() || name.isEmpty()){
                                Toast.makeText(AdminAddCourseActivity.this, "You Must Complete all Fields", Toast.LENGTH_SHORT).show();
                            }
                            else if (!(offerings.get(0)||offerings.get(1)||offerings.get(2))){
                                Toast.makeText(AdminAddCourseActivity.this, "You Must Choose At Least 1 Offering", Toast.LENGTH_SHORT).show();
                            }
                            else if (task.getResult().exists()){
                                Toast.makeText(AdminAddCourseActivity.this, "The Course: " + code + " already exists", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Course course = new Course(name, code, offerings, prereqs);
                                ref.child(code).setValue(course).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        binding.adminAddCourseName.setText("");
                                        binding.adminAddCourseCode.setText("");
                                        binding.fallSwitch.setChecked(false);
                                        binding.winterSwitch.setChecked(false);
                                        binding.summerSwitch.setChecked(false);
                                        int i = 0;
                                        while (prereq_search.get(i).getSelected()){
                                            prereq_search.get(i).setSelected(false);
                                            i++;
                                        }
                                        filterList(binding.searchView.getQuery().toString());
                                        CourseSearchItemAdapter searchAdapter = new CourseSearchItemAdapter(context, courseFilter);
                                        rvs.setAdapter((searchAdapter));
                                        Toast.makeText(AdminAddCourseActivity.this, "Successfully Added Course: " + code, Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    }
                });
            }
        }));





        ref = database.getReference("Courses");
        ref.orderByKey().addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                prereq_search.add(prereq_search.size(),new CourseSearchItem(snapshot.getKey().toString(), false));
                filterList(binding.searchView.getQuery().toString());
                CourseSearchItemAdapter searchAdapter = new CourseSearchItemAdapter(context, courseFilter);
                rvs.setAdapter((searchAdapter));

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                for (int i = 0; i < prereq_search.size(); i++) {
                    CourseSearchItem currentCourse = prereq_search.get(i);
                    if (currentCourse.getCode().equals(snapshot.getKey().toString())){
                        prereq_search.remove(i);
                        break;
                    }
                }
                filterList(binding.searchView.getQuery().toString());
                CourseSearchItemAdapter searchAdapter = new CourseSearchItemAdapter(context, courseFilter);
                rvs.setAdapter((searchAdapter));

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }



    public void filterList(String text){
        courseFilter = new ArrayList<CourseSearchItem>();
        for (CourseSearchItem course:prereq_search){
            if (course.getCode().toUpperCase().contains(text.toUpperCase())){
                courseFilter.add(course);
            }
        }
        CourseSearchItemAdapter searchAdapter = new CourseSearchItemAdapter(context, courseFilter);
        rvs.setAdapter((searchAdapter));
    }
}
