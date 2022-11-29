package com.example.courseplannerapp;

public class CourseSearchItem {

    private String code;
    private boolean selected;

    public CourseSearchItem(String code, boolean selected) {
        this.code = code;
        this.selected = selected;
    }

    public String getCode() {
        return code;
    }

    public boolean getSelected() {
        return selected;
    }
    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}

//    FirebaseDatabase db;
//    DatabaseReference reference;
//    String course;
//    Context context;
//    boolean selected;

//    @Override
//    public void onClick(View view) {
//        if(!selected) {
//            db = FirebaseDatabase.getInstance();
//            course = this.getText().toString().toUpperCase();
//
//            //Add to selected data
//            reference = db.getReference("coursesSelected");
//            reference.child(course).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
//                @Override
//                public void onComplete(@NonNull Task<DataSnapshot> task) {
//                    if(task.isSuccessful()){
//                        reference.child(course).setValue(course);
//                        Toast.makeText(context, course+ " Added", Toast.LENGTH_SHORT).show();
//                    } else{
//                        Toast.makeText(context, "An error has occurred", Toast.LENGTH_SHORT).show();
//                    }
//                }
//            });
//            //Remove from unselected data
//            reference = db.getReference("coursesUnselected");
//            reference.child(course).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
//                @Override
//                public void onComplete(@NonNull Task<DataSnapshot> task) {
//                    if (task.isSuccessful()) {
//                        reference.child(course).removeValue();
//                        Toast.makeText(context, course + " Removed", Toast.LENGTH_SHORT).show();
//                    } else {
//                        Toast.makeText(context, "An error has occurred", Toast.LENGTH_SHORT).show();
//                    }
//                }
//            });
//
//            selected = true;
//            this.setBackgroundResource(R.color.uoft_dark);
//        }
//        else {
//            db = FirebaseDatabase.getInstance();
//            course = this.getText().toString().toUpperCase();
//
//            //Add to selected data
//            reference = db.getReference("coursesUnselected");
//            reference.child(course).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
//                @Override
//                public void onComplete(@NonNull Task<DataSnapshot> task) {
//                    if(task.isSuccessful()){
//                        reference.child(course).setValue(course);
//                        Toast.makeText(context, course+ " Added", Toast.LENGTH_SHORT).show();
//                    } else{
//                        Toast.makeText(context, "An error has occurred", Toast.LENGTH_SHORT).show();
//                    }
//                }
//            });
//            //Remove from unselected data
//            reference = db.getReference("coursesSelected");
//            reference.child(course).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
//                @Override
//                public void onComplete(@NonNull Task<DataSnapshot> task) {
//                    if (task.isSuccessful()) {
//                        reference.child(course).removeValue();
//                        Toast.makeText(context, course + " Removed", Toast.LENGTH_SHORT).show();
//                    } else {
//                        Toast.makeText(context, "An error has occurred", Toast.LENGTH_SHORT).show();
//                    }
//                }
//            });
//
//            selected = true;
//            this.setBackgroundResource(R.color.uoft_dark);
//        }
//    }
