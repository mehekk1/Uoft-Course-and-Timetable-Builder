package com.example.courseplannerapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class CourseSearchItemAdapter extends RecyclerView.Adapter<CourseSearchItemAdapter.ViewHolder> {

    Context context;
    ArrayList<CourseSearchItem> courses;

    public CourseSearchItemAdapter(Context context, ArrayList<CourseSearchItem> courses) {
        this.context = context;
        this.courses = courses;
    }

    @NonNull
    @Override
    public CourseSearchItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);

        View courseView = inflater.inflate(R.layout.future_course_search_item, parent, false);

        ViewHolder viewHolder = new CourseSearchItemAdapter.ViewHolder(courseView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CourseSearchItemAdapter.ViewHolder holder, int position) {
        CourseSearchItem course = courses.get(position);

        holder.codeTextView.setText(course.getCode());
        if (!course.getSelected()) {
            holder.codeTextView.setBackgroundResource(R.color.white);
        }
        else {
            holder.codeTextView.setBackgroundResource(R.color.uoft_dark);
        }


    }

    @Override
    public int getItemCount() {
        return courses.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public TextView codeTextView;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            codeTextView = itemView.findViewById(R.id.item_name);

        }
    }
}
