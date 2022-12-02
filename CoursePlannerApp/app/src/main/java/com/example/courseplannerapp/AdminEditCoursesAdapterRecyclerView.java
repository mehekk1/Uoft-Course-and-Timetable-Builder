package com.example.courseplannerapp;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdminEditCoursesAdapterRecyclerView extends RecyclerView.Adapter<AdminEditCoursesAdapterRecyclerView.MyViewHolder> {

    Context context;
    ArrayList<Course> courseModel;

    public AdminEditCoursesAdapterRecyclerView(Context context, ArrayList<Course> courseModel){
        this.context = context;
        this.courseModel = courseModel;
    }

    @NonNull
    @Override
    public AdminEditCoursesAdapterRecyclerView.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//  inflating layout and giving look to each row
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.admin_recycler_view_row, parent, false);

        return new AdminEditCoursesAdapterRecyclerView.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminEditCoursesAdapterRecyclerView.MyViewHolder holder, int position) {
//   Assigning values to each of the rows

        holder.courseName.setText(courseModel.get(position).getName());
        holder.courseCode.setText(courseModel.get(position).getCode());

    }

    @Override
    public int getItemCount() {
//  How many items in total
        return courseModel.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;

        TextView courseName, courseCode;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            courseCode = itemView.findViewById(R.id.course_code_card_text);
            courseName = itemView.findViewById(R.id.course_name_card_text);
            imageView = itemView.findViewById(R.id.edit_card_img);
            imageView.setImageResource(R.drawable.uoft_logo_black);


        }
    }
}
