package com.example.courseplannerapp;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

//import com.example.courseplannerapp.databinding.ActivityTakenBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Locale;
import java.util.zip.Inflater;

public class TakenTimelineActivity extends AppCompatActivity {

    //private ActivityTakenTimeline binding;
    FirebaseDatabase db;
    DatabaseReference reference;
    String course;
    ArrayList<String> courses;
    Button b;
    Button b2;
    EditText e;
    Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_taken_timeline);
        context = this.getApplicationContext();
        b = findViewById(R.id.button_first);
        b2 = findViewById(R.id.button_second);
        e = findViewById(R.id.course_text);
        db = FirebaseDatabase.getInstance();
        courses = new ArrayList<String>();
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db = FirebaseDatabase.getInstance();
                course = e.getText().toString().toUpperCase();
                reference = db.getReference("courses");
                reference.child(course).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if(task.isSuccessful()){
                            if (task.getResult().exists()){
                                Toast.makeText(context, course + " already exists", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                reference.child(course).setValue(course);
                                Toast.makeText(context, course+ " Added", Toast.LENGTH_SHORT).show();
                            }
                        } else{
                            Toast.makeText(context, "An error has occurred", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db = FirebaseDatabase.getInstance();
                course = e.getText().toString().toUpperCase();
                reference = db.getReference("courses");
                reference.child(course).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult().exists()) {
                                reference.child(course).removeValue();
                                Toast.makeText(context, course + " Removed", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(context, course + " Not In List", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(context, "An error has occurred", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        reference = db.getReference("courses");
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
    }
    /*
    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        //binding = FragmentFirstBinding.inflate(inflater, container, false);
        View itemLayout = inflater.inflate(R.layout.activity_taken_timeline, null, false);
        b = itemLayout.findViewById(R.id.button_first);
        b2 = itemLayout.findViewById(R.id.button_second);
        e = itemLayout.findViewById(R.id.course_text);
        db = FirebaseDatabase.getInstance();
        courses = new ArrayList<String>();
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                course = e.getText().toString().toUpperCase(Locale.ROOT);
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
        return itemLayout;
    }*/

    /*public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db = FirebaseDatabase.getInstance();
                course = e.getText().toString().toUpperCase();
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
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db = FirebaseDatabase.getInstance();
                course = e.getText().toString().toUpperCase();
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
    }*/



    public void init(){
        TableLayout stk = findViewById(R.id.table_main);
        int count = stk.getChildCount();
        for (int i = 0; i < count; i++) {
            View child = stk.getChildAt(i);
            if (child instanceof TableRow) ((ViewGroup) child).removeAllViews();
        }
        TableRow tbrow0 = new TableRow(context);
        TextView tv0 = new TextView(context);
        tv0.setText("You have taken " + courses.size() + " courses!");
        tv0.setTextSize(24);
        tbrow0.addView(tv0);
        stk.addView(tbrow0);
        for(String c:courses){
            TableRow tbrow = new TableRow(context);
            TextView tv1 = new TextView(context);
            tv1.setText(c);
            tv0.setTextSize(18);
            tbrow.addView(tv1);
            stk.addView(tbrow);
        }
    }
}