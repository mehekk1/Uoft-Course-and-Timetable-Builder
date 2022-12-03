package com.example.courseplannerapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class GenerateTimelineAdapter extends RecyclerView.Adapter<GenerateTimelineAdapter.ViewHolder> {

    Context context;
    ArrayList<String> yearAndSem;
    ArrayList<ArrayList<String>> coursesToTake;

    public GenerateTimelineAdapter(Context context, ArrayList<String> yearAndSem, ArrayList<ArrayList<String>> coursesToTake) {
        this.context = context;
        this.yearAndSem = yearAndSem;
        this.coursesToTake = coursesToTake;
    }

    @NonNull
    @Override
    public GenerateTimelineAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);

        View courseView = inflater.inflate(R.layout.generate_recycler_view_row, parent, false);

        ViewHolder viewHolder = new GenerateTimelineAdapter.ViewHolder(courseView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(GenerateTimelineAdapter.ViewHolder holder, int position) {
        String curYearSem = yearAndSem.get(position);
        ArrayList<String> curCoursesToTake = coursesToTake.get(position);

        String year = curYearSem.substring(0,curYearSem.length()-1);
        String sem;
        if(curYearSem.substring(curYearSem.length()-1).equals("0")) {
            sem = "Winter";
        }
        else if(curYearSem.substring(curYearSem.length()-1).equals("1")) {
            sem = "Summer";
        }
        else {
            sem = "Fall";
        }
        holder.takeDateTV.setText(sem + " " + year);

        for(int i = 0; i < curCoursesToTake.size(); i++) {
            TextView tv = new TextView(context);
            tv.setId(i + 5);
            holder.courses.add(tv);
            holder.constraintLayout.addView(tv);
        }

        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(holder.constraintLayout);

        if(curCoursesToTake.size() == 1) {
            holder.courses.get(0).setText(curCoursesToTake.get(0));
            constraintSet.connect(holder.courses.get(0).getId(), ConstraintSet.TOP, R.id.parent_layout, ConstraintSet.TOP);
            constraintSet.setMargin(holder.courses.get(0).getId(), ConstraintSet.TOP, 8);
            constraintSet.connect(holder.courses.get(0).getId(), ConstraintSet.BOTTOM, R.id.parent_layout, ConstraintSet.BOTTOM);
            constraintSet.setMargin(holder.courses.get(0).getId(), ConstraintSet.BOTTOM, 8);
            constraintSet.connect(holder.courses.get(0).getId(), ConstraintSet.END, R.id.parent_layout, ConstraintSet.END);
            constraintSet.setMargin(holder.courses.get(0).getId(), ConstraintSet.END, 92);
        }
        else {
            int i = 0;
            while(i < curCoursesToTake.size()) {
                if(i == 0) {
                    constraintSet.connect(holder.courses.get(i).getId(), ConstraintSet.TOP, R.id.parent_layout, ConstraintSet.TOP);
                    constraintSet.setMargin(holder.courses.get(i).getId(), ConstraintSet.TOP, 8);
                    constraintSet.connect(holder.courses.get(i).getId(), ConstraintSet.BOTTOM, holder.courses.get(i+1).getId(), ConstraintSet.TOP);
                }
                else if(i == curCoursesToTake.size()-1) {
                    constraintSet.connect(holder.courses.get(i).getId(), ConstraintSet.TOP, holder.courses.get(i-1).getId(), ConstraintSet.BOTTOM);
                    constraintSet.connect(holder.courses.get(i).getId(), ConstraintSet.BOTTOM, R.id.parent_layout, ConstraintSet.BOTTOM);
                    constraintSet.setMargin(holder.courses.get(i).getId(), ConstraintSet.BOTTOM, 8);
                }
                else {

                    constraintSet.connect(holder.courses.get(i).getId(), ConstraintSet.TOP, holder.courses.get(i-1).getId(), ConstraintSet.BOTTOM);
                    constraintSet.connect(holder.courses.get(i).getId(), ConstraintSet.BOTTOM, holder.courses.get(i+1).getId(), ConstraintSet.TOP);
                }
                holder.courses.get(i).setText(curCoursesToTake.get(i));
                constraintSet.connect(holder.courses.get(i).getId(), ConstraintSet.END, R.id.parent_layout, ConstraintSet.END);
                constraintSet.setMargin(holder.courses.get(i).getId(), ConstraintSet.END, 92);
                i++;
            }
        }

        constraintSet.applyTo(holder.constraintLayout);
    }

    @Override
    public int getItemCount() {
        return yearAndSem.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public TextView takeDateTV;

        public ArrayList<TextView> courses;



        ConstraintLayout constraintLayout;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            takeDateTV = itemView.findViewById(R.id.year_and_sem);

            courses = new ArrayList<TextView>(coursesToTake.size());

            constraintLayout = itemView.findViewById(R.id.parent_layout);
        }
    }
}
