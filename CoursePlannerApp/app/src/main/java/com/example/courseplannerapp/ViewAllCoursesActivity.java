package com.example.courseplannerapp;

import static com.example.courseplannerapp.R.color.black;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ViewAllCoursesActivity extends AppCompatActivity {

    Context context;
    FirebaseDatabase db;
    DatabaseReference reference;
    String offerings;
    private BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_courses);
        context = this.getApplicationContext();
        db = FirebaseDatabase.getInstance();
        init();
    }

    public void init(){
        TableLayout stk = findViewById(R.id.table_main);
        TableRow tbrow0 = new TableRow(context);
        TextView tv0 = new TextView(context);
        tv0.setText(" Course Code    ");
        tv0.setTextColor(Color.BLACK);
        tv0.setTextSize(15);
        tv0.setTypeface(null, Typeface.BOLD);
        tbrow0.addView(tv0);
        TextView tv1 = new TextView(context);
        tv1.setText(" Offering Sessions ");
        tv1.setTextColor(Color.BLACK);
        tv1.setTextSize(15);
        tv1.setTypeface(null, Typeface.BOLD);
        tbrow0.addView(tv1);
        TextView tv2 = new TextView(context);
        tv2.setText(" Prerequesites ");
        tv2.setTextColor(Color.BLACK);
        tv2.setTextSize(15);
        tv2.setTypeface(null, Typeface.BOLD);
        tbrow0.addView(tv2);
        stk.addView(tbrow0);
        reference = db.getReference("CoursesTestVedat");
        reference.orderByKey().addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                TableRow tbrow = new TableRow(context);
                TextView t1v = new TextView(context);
                String course = snapshot.getKey();
                t1v.setText(course);
                t1v.setTextColor(Color.BLACK);
                t1v.setGravity(Gravity.CENTER);
                tbrow.addView(t1v);
                TextView t2v = new TextView(context);
                t2v.setText("");
                DatabaseReference offers = reference.child(course).child("offerings");
                offers.child("Fall").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot2) {
                        if((Boolean)snapshot2.getValue())
                            t2v.setText(t2v.getText().toString() + "Fall, ");
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                offers.child("Winter").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot2) {
                        if((Boolean)snapshot2.getValue())
                            t2v.setText(t2v.getText().toString() + "Winter, ");
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                offers.child("Summer").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot2) {
                        if((Boolean)snapshot2.getValue())
                            t2v.setText(t2v.getText().toString() + "Summer, ");
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                String display = t2v.getText().toString();
                t2v.setText(display);
                t2v.setTextColor(Color.BLACK);
                t2v.setGravity(Gravity.CENTER);
                tbrow.addView(t2v);
                TextView t3v = new TextView(context);
                reference.child(course).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot5) {
                        if(snapshot5.hasChild("prereqs")) {
                            DatabaseReference prereqs = reference.child(course).child("prereqs");
                            prereqs.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot3) {
                                    t3v.setText(snapshot3.getValue().toString());
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }
                        else{
                            t3v.setText("None");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                t3v.setWidth(100);
                t3v.setTextColor(Color.BLACK);
                t3v.setGravity(Gravity.CENTER);
                tbrow.addView(t3v);
                stk.addView(tbrow);
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

}
