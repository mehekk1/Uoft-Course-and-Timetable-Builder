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

        int i = 0;
        while(i < curCoursesToTake.size() && i < 6) {
            holder.courses.get(i).setText(curCoursesToTake.get(i));
            System.out.println(curCoursesToTake.get(i));
            i++;
        }
        i--;

        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(holder.constraintLayout);
        constraintSet.connect(holder.courses.get(i).getId(), ConstraintSet.BOTTOM, R.id.parent_layout, ConstraintSet.BOTTOM);
        constraintSet.setMargin(holder.courses.get(i).getId(), ConstraintSet.BOTTOM, 8);
        constraintSet.applyTo(holder.constraintLayout);

        i++;
        while(i < 6 || i < curCoursesToTake.size()) {
            holder.courses.get(i).setVisibility(View.GONE);
            i++;
        }

    }

    @Override
    public int getItemCount() {
        return yearAndSem.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public TextView takeDateTV;

        public TextView course1;
        public TextView course2;
        public TextView course3;
        public TextView course4;
        public TextView course5;
        public TextView course6;
        public ArrayList<TextView> courses;
        ConstraintLayout constraintLayout;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            takeDateTV = itemView.findViewById(R.id.year_and_sem);

            course1 = itemView.findViewById(R.id.course1);
            course2 = itemView.findViewById(R.id.course2);
            course3 = itemView.findViewById(R.id.course3);
            course4 = itemView.findViewById(R.id.course4);
            course5 = itemView.findViewById(R.id.course5);
            course6 = itemView.findViewById(R.id.course6);

            courses = new ArrayList<TextView>();
            courses.add(course1);
            courses.add(course2);
            courses.add(course3);
            courses.add(course4);
            courses.add(course5);
            courses.add(course6);

            constraintLayout = itemView.findViewById(R.id.parent_layout);
        }
    }
}
