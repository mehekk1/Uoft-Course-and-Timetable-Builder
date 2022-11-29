package com.example.courseplannerapp;

import static com.example.courseplannerapp.R.color.black;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class TakenTimelineActivity extends AppCompatActivity {

    FirebaseDatabase db;
    DatabaseReference reference;
    DatabaseReference sec_ref;
    String course;
    ArrayList<String> courses;
    Button b;
    Button b2;
    EditText e;
    Context context;
    String student;


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
        student = "charles";
        init();
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                course = e.getText().toString().toUpperCase();
                reference = db.getReference("Students");
                sec_ref = db.getReference("Courses");
                reference.child(student).child("taken_list").child(course).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if(task.isSuccessful()){
                            if(e.getText().toString().isEmpty()){
                                Toast.makeText(context, course + "Please enter a course code", Toast.LENGTH_SHORT).show();
                            }
                            else if (task.getResult().exists()){
                                Toast.makeText(context, course + " already exists", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                sec_ref.child(course).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DataSnapshot> task2) {
                                        if (task2.getResult().exists()){
                                            reference.child(student).child("taken_list").child(course).setValue(course);
                                            Toast.makeText(context, course+ " Added", Toast.LENGTH_SHORT).show();
                                        }
                                        else
                                            Toast.makeText(context, "This course does not exist, please try again", Toast.LENGTH_SHORT).show();
                                    }
                                });
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
                reference = db.getReference("Students");
                reference.child(student).child("taken_list").child(course).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (task.isSuccessful()) {
                            if(e.getText().toString().isEmpty()){
                                Toast.makeText(context, course + "Please enter a course code", Toast.LENGTH_SHORT).show();
                            }
                            else if (task.getResult().exists()) {
                                reference.child(student).child("taken_list").child(course).removeValue();
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
        reference = db.getReference("Students");
        reference.child(student).child("taken_list").orderByKey().addChildEventListener(new ChildEventListener() {
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

    public void init(){
        TableLayout stk = findViewById(R.id.table_main);
        int count = stk.getChildCount();
        for (int i = 0; i < count; i++) {
            View child = stk.getChildAt(i);
            if (child instanceof TableRow) ((ViewGroup) child).removeAllViews();
        }
        TableRow tbrow0 = new TableRow(context);
        TextView tv0 = new TextView(context);
        int num = courses.size();
        if(num==1)
            tv0.setText("You have taken " + 1 + " course!");
        else
            tv0.setText("You have taken " + num + " courses!");
        tv0.setTextSize(28);
        tv0.setTextColor(getResources().getColor(black));
        tv0.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        tv0.setTypeface(null, Typeface.BOLD);
        tbrow0.addView(tv0);
        stk.addView(tbrow0);
        for(String c:courses){
            TableRow tbrow = new TableRow(context);
            TextView tv1 = new TextView(context);
            tv1.setTextColor(getResources().getColor(black));
            tv1.setText(c);
            tv1.setTextSize(20);
            tv1.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            tv1.setTypeface(null, Typeface.BOLD);
            tbrow.addView(tv1);
            stk.addView(tbrow);
        }
    }

}