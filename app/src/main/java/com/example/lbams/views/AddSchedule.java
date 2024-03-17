package com.example.lbams.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.DatePickerDialog;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;

import com.example.lbams.R;
import com.example.lbams.adapters.ScheduleAdapter;
import com.example.lbams.databinding.ActivityAddScheduleBinding;
import com.example.lbams.model.AttendanceLocationList;
import com.example.lbams.model.ScheduleModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import org.checkerframework.checker.units.qual.C;
import org.checkerframework.checker.units.qual.Time;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class AddSchedule extends BaseActvity {
      ActivityAddScheduleBinding binding;
      ArrayList<String> classList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddScheduleBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        populateTime();
        getLocation();
        binding.selectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });
       binding.submitButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               String ClassCode = binding.spinnerClass.getSelectedItem().toString();
               ScheduleExistedDataList(ClassCode);
           }
       });
    }
    private void populateTime(){
        ArrayList<String> times = generateTimes();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, times);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.SpinnerTime.setAdapter(adapter);
    }
    private void showDatePickerDialog() {
        Calendar calendar = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            calendar = Calendar.getInstance();
        }
        int year = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            year = calendar.get(Calendar.YEAR);
        }
        int month = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            month = calendar.get(Calendar.MONTH);
        }
        int day = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            day = calendar.get(Calendar.DAY_OF_MONTH);
        }

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        // Set the selected date to the EditText
                        String selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;
                        binding.editTextDatePicker.setText(selectedDate);
                    }
                },
                year, month, day);
        datePickerDialog.show();
    }
    public static ArrayList<String> generateTimes() {
        ArrayList<String> times = new ArrayList<>();
        for (int hour = 1; hour <= 12; hour++) {
            for (int minute = 0; minute < 60; minute += 15) {
                String formattedHour = String.format("%02d", hour);
                String formattedMinute = String.format("%02d", minute);
                times.add(formattedHour + ":" + formattedMinute + " AM");
            }
        }
        for (int hour = 1; hour <= 12; hour++) {
            for (int minute = 0; minute < 60; minute += 15) {
                String formattedHour = String.format("%02d", hour);
                String formattedMinute = String.format("%02d", minute);
                times.add(formattedHour + ":" + formattedMinute + " PM");
            }
        }
        return times;
    }
    private void getLocation() {
        dbRef.child("MultiLocation").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                classList = new ArrayList<>();
                for(DataSnapshot snapshot1:snapshot.getChildren()){
                    AttendanceLocationList model = snapshot1.getValue(AttendanceLocationList.class);
                    assert model != null;
                    Log.d("Dab",model.ClassCode);
                    String Class  = model.ClassCode;
                    classList.add(Class);
                }
                classList.add(0,"Please Select Class");
               populateClass(classList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
    private void populateClass(ArrayList<String> list){
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerClass.setAdapter(adapter);
    }
    private void SaveSchedule(ArrayList<ScheduleModel> dataList){
        boolean isScheduleExisted = false;

       String ClassCode = binding.spinnerClass.getSelectedItem().toString();
       String ClassName  = binding.className.getText().toString();
       String day = binding.spinnerDay.getSelectedItem().toString();
       String Date = binding.editTextDatePicker.getText().toString();
       String time = binding.SpinnerTime.getSelectedItem().toString();

       //

        for (ScheduleModel model : dataList){
            if(model.Date.equals(Date) && model.classCode.equals(ClassCode) && model.Time.equals(time)){
                isScheduleExisted = true;
                break;
            }
        }
        //
       ScheduleModel schedule = new ScheduleModel(ClassCode,ClassName, day, Date, time);

       if(isScheduleExisted){
           binding.progress.setVisibility(View.GONE);
           showCustomDialog("This class Schedule is already existed ");
       }else{
           dbRef.child("Schedule").child(ClassCode).child(getRandomId()).setValue(schedule).addOnCompleteListener(new OnCompleteListener<Void>() {
               @Override
               public void onComplete(@NonNull Task<Void> task) {
                   binding.progress.setVisibility(View.GONE);
                   showCustomDialog("Schedule Added Successfully!");
               }
           });
       }

    }
    private  String getRandomId() {
        LocalDateTime now = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            now = LocalDateTime.now();
        }

        // Define the desired format
        DateTimeFormatter formatter = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        }

        // Format date and time in the desired format
        String formattedDateTime = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            formattedDateTime = now.format(formatter);
        }

        return  formattedDateTime;
    }


    public  void ScheduleExistedDataList(String code){
        binding.progress.setVisibility(View.VISIBLE);
        ArrayList<ScheduleModel> dataList = new ArrayList<>();
        dbRef.child("Schedule").child(code).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot scheduleSnap : snapshot.getChildren()) {
                    ScheduleModel dataModel = scheduleSnap.getValue(ScheduleModel.class);
                    Log.d("DAB", dataModel.className);
                    dataList.add(dataModel);
                }
              SaveSchedule(dataList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}