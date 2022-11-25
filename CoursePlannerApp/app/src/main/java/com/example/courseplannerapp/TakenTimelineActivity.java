package com.example.tester;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.tester.databinding.FragmentFirstBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Locale;

public class TakenTimelineActivity extends Fragment {

    private FragmentFirstBinding binding;
    FirebaseDatabase db;
    DatabaseReference reference;
    String course;
    ArrayList<String> courses;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FragmentFirstBinding.inflate(inflater, container, false);
        db = FirebaseDatabase.getInstance();
        courses = new ArrayList<String>();
        binding.buttonFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                course = binding.courseText.getText().toString().toUpperCase(Locale.ROOT);
                reference = db.getReference("Courses");
                reference.child(course).setValue(course);
            }
        });
        reference = db.getReference("Courses");
        reference.orderByKey().addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                courses.add(snapshot.getValue().toString());
                init();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                courses.remove(snapshot.getValue().toString());
                init();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.buttonFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db = FirebaseDatabase.getInstance();
                course = binding.courseText.getText().toString().toUpperCase();
                reference = db.getReference("Courses");
                reference.child(course).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if(task.isSuccessful()){
                            if (task.getResult().exists()){
                                Toast.makeText(getContext(), course + " already exists", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                reference.child(course).setValue(course);
                                Toast.makeText(getContext(), course+ " Added", Toast.LENGTH_SHORT).show();
                            }
                        } else{
                            Toast.makeText(getContext(), "An error has occurred", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        binding.buttonSecond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db = FirebaseDatabase.getInstance();
                course = binding.courseText.getText().toString().toUpperCase();
                reference = db.getReference("Courses");
                reference.child(course).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult().exists()) {
                                reference.child(course).removeValue();
                                Toast.makeText(getContext(), course + " Removed", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getContext(), course + " Not In List", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getContext(), "An error has occurred", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void init(){
        int count = binding.tableMain.getChildCount();
        for (int i = 0; i < count; i++) {
            View child = binding.tableMain.getChildAt(i);
            if (child instanceof TableRow) ((ViewGroup) child).removeAllViews();
        }
        TableLayout stk = binding.tableMain;
        TableRow tbrow0 = new TableRow(getContext());
        TextView tv0 = new TextView(getContext());
        tv0.setText("You have taken " + courses.size() + " courses!");
        tv0.setTextSize(24);
        tbrow0.addView(tv0);
        stk.addView(tbrow0);
        for(String c:courses){
            TableRow tbrow = new TableRow(getContext());
            TextView tv1 = new TextView(getContext());
            tv1.setText(c);
            tv0.setTextSize(18);
            tbrow.addView(tv1);
            stk.addView(tbrow);
        }
    }
}