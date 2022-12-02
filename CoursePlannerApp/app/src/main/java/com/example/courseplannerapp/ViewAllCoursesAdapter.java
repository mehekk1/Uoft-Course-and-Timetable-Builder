package com.example.courseplannerapp;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ViewAllCoursesAdapter extends RecyclerView.Adapter<ViewAllCoursesAdapter.ViewHolder> {

    Context context;
    ArrayList<Course> courses;

    public ViewAllCoursesAdapter(Context context, ArrayList<Course> courses){
        this.context = context;
        this.courses = courses;
    }

    @NonNull
    @Override
    public ViewAllCoursesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.view_all_course_row, parent, false);

        return new ViewAllCoursesAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewAllCoursesAdapter.ViewHolder holder, int position) {

        holder.courseName.setText(courses.get(position).getName());
        holder.courseCode.setText(courses.get(position).getCode());

    }

    @Override
    public int getItemCount() {
        return courses.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        ImageView course_uni_img;
        TextView courseName, courseCode;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            courseName = itemView.findViewById(R.id.view_all_course_name);
            courseCode = itemView.findViewById(R.id.view_all_course_code);
            course_uni_img = itemView.findViewById(R.id.view_all_course_img);
            course_uni_img.setImageResource(R.drawable.uoft);


        }
    }
}
