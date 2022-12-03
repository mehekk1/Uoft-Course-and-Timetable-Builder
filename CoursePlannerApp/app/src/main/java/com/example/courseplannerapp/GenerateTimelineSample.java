package com.example.courseplannerapp;

import static com.example.courseplannerapp.R.color.black;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

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
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class GenerateTimelineSample extends AppCompatActivity {

    FirebaseDatabase db;
    DatabaseReference taken_list;
    DatabaseReference reference;
    HashMap<String, String> courseToSem;
    ArrayList<String> toTake;
    String currentSem;
    Context context;
    ArrayList<String> prereqs;
    int max;
    ArrayList<Boolean> semesters;
    ArrayList<Integer> when;
    HashMap<String, ArrayList<String>> prereq;
    HashMap<String, ArrayList<Boolean>> offers;
    String last_taken;
    Button b;
    ArrayList<String> s;
    ArrayList<Boolean> o;
    ArrayList<String> takenCourses;

    HashMap<String, String> timeline;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_timeline_sample);
        context = getApplicationContext();
        courseToSem = new HashMap<String, String>();
        toTake = new ArrayList<String>();
        toTake.add("MATA37");
        currentSem = "20223";
        db = FirebaseDatabase.getInstance();
        timeline = new HashMap<String, String>();
        taken_list = db.getReference("Students");
        taken_list.child("test").child("taken_list").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                GenericTypeIndicator<ArrayList<String>> lol = new GenericTypeIndicator<ArrayList<String>>() {};
                takenCourses = task.getResult().getValue(lol);
                for(String c:takenCourses){
                    courseToSem.put(c, currentSem);
                }
            }
        });

        offers = new HashMap<String, ArrayList<Boolean>>();
        prereq = new HashMap<String, ArrayList<String>>();
        reference = db.getReference("Courses");
        reference.orderByKey().addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                offers.put(snapshot.child("code").getValue(String.class), o);
                String course = snapshot.child("code").getValue(String.class);
                DatabaseReference r = reference.child(course).child("offerings");

                r.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if(task!=null) {
                            GenericTypeIndicator<ArrayList<Boolean>> lol = new GenericTypeIndicator<ArrayList<Boolean>>() {};
                            o = task.getResult().getValue(lol);
                            if(s==null)
                                o=new ArrayList<Boolean>();
                            offers.put(course, o);
                        }
                    }
                });


                s = new ArrayList<String>();
                prereq.put(course, s);
                reference.child(course).child("prereqs").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if(task!=null) {
                            GenericTypeIndicator<ArrayList<String>> lol = new GenericTypeIndicator<ArrayList<String>>() {};
                            s = task.getResult().getValue(lol);
                            if(s==null)
                                s=new ArrayList<String>();
                            prereq.put(course, s);
                        }
                    }
                });


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
        b = findViewById(R.id.button1);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                generateTimeline();
            }
        });
    }

    public void generateTimeline() {
        currentSem = "20223";

        while(!toTake.isEmpty()){
            String current_course=toTake.get(toTake.size()-1);
            if(courseToSem.containsKey(current_course)){
                toTake.remove(toTake.size()-1);
                continue;
            }
            boolean isTop = true;
            prereqs = prereq.get(current_course);
            for(String p: prereqs) {
                if (!courseToSem.containsKey(p)) {
                    toTake.add(p);
                    isTop = false;
                }
            }
            if (isTop){
                int earliest;
                max = Integer.parseInt(currentSem);
                for(String c:prereqs) {
                    int cur = Integer.parseInt(courseToSem.get(c));
                    if(cur>max)
                        max = cur;
                }
                semesters = new ArrayList<Boolean>();
                last_taken = Integer.toString(max);

                semesters = offers.get(current_course);

                when = new ArrayList<Integer>();
                if(semesters.get(2)){
                    if (last_taken.charAt(4) == '1')
                        when.add(max + 10);
                    else if (last_taken.charAt(4) == '2')
                        when.add(max + 9);
                    else
                        when.add(max + 8);
                }
                else
                    when.add(696969);
                if(semesters.get(1)){
                    if (last_taken.charAt(4) == '1')
                        when.add(max + 2);
                    else if (last_taken.charAt(4) == '2')
                        when.add(max + 9);
                    else
                        when.add(max + 8);
                }
                else
                    when.add(696969);
                if(semesters.get(0)){
                    if (last_taken.charAt(4) == '1')
                        when.add(max + 1);
                    else if (last_taken.charAt(4) == '2')
                        when.add(max + 10);
                    else
                        when.add(max + 9);
                }
                else
                    when.add(696969);

                earliest = Math.min(when.get(0),when.get(1));
                earliest = Math.min(earliest,when.get(2));
                courseToSem.put(current_course, Integer.toString(earliest));
                timeline.put(current_course, Integer.toString(earliest));
                toTake.remove(toTake.size()-1);
            }
        }
        init();
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
        tv0.setText("Here is your timeline");
        tv0.setTextSize(28);
        tv0.setTextColor(getResources().getColor(black));
        tv0.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        tv0.setTypeface(null, Typeface.BOLD);
        tbrow0.addView(tv0);
        stk.addView(tbrow0);
        for(String key:timeline.keySet()){
            TableRow tbrow = new TableRow(context);
            TextView tv1 = new TextView(context);
            tv1.setTextColor(getResources().getColor(black));
            String builder = "";
            tv1.setText(key+ " " + timeline.get(key));
            tv1.setTextSize(20);
            tv1.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            tv1.setTypeface(null, Typeface.BOLD);
            tbrow.addView(tv1);
            stk.addView(tbrow);
        }
    }
}

