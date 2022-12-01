//package com.example.courseplannerapp;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//
//import android.os.Bundle;
//import android.view.View;
//import android.widget.CompoundButton;
//import android.widget.Toast;
//
//import com.example.courseplannerapp.databinding.ActivityAdminAddCourseBinding;
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.Task;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//
//import java.util.List;
//import java.util.Locale;
//
//public class AdminAddCourseActivity extends AppCompatActivity {
//    ActivityAdminAddCourseBinding binding;
//    String code, name;
//    boolean fall, winter, summer;
//    List<Course> prereqs;
//    FirebaseDatabase database;
//    DatabaseReference ref;
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        binding = ActivityAdminAddCourseBinding.inflate(getLayoutInflater());
//        setContentView(binding.getRoot());
//
//        binding.adminAddCourseBtn.setOnClickListener((new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                code = binding.adminAddCourseCode.getText().toString().toUpperCase();
//                name = binding.adminAddCourseName.getText().toString();
//                fall = binding.fallSwitch.isChecked();
//                winter = binding.winterSwitch.isChecked();
//                summer = binding.summerSwitch.isChecked();
//
//                if (!code.isEmpty() && !name.isEmpty() && (fall||winter||summer)){
//                    Course course = new Course(name, code, fall, winter, summer);
//                    database = FirebaseDatabase.getInstance();
//
//                    ref = database.getReference("AdminCourses");
//                    ref.child(code).setValue(course).addOnCompleteListener(new OnCompleteListener<Void>() {
//                        @Override
//                        public void onComplete(@NonNull Task<Void> task) {
//
//                            binding.adminAddCourseName.setText("");
//                            binding.adminAddCourseCode.setText("");
//                            binding.fallSwitch.setChecked(false);
//                            binding.winterSwitch.setChecked(false);
//                            binding.summerSwitch.setChecked(false);
//                            Toast.makeText(AdminAddCourseActivity.this, "Successfully Added Course", Toast.LENGTH_SHORT).show();
//                        }
//                    });
//
//                }
//                else if (!code.isEmpty() && !name.isEmpty() && !(fall||winter||summer)){
//                    Toast.makeText(AdminAddCourseActivity.this, "You Must Choose At Least 1 Offering", Toast.LENGTH_SHORT).show();
//                }
//                else{
//                    Toast.makeText(AdminAddCourseActivity.this, "You Must Fill all Fields", Toast.LENGTH_SHORT).show();
//                }
//            }
//        }));
//    }
//}