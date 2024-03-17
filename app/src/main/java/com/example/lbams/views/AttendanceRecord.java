package com.example.lbams.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;

import com.example.lbams.R;
import com.example.lbams.adapters.ScheduleAdapter;
import com.example.lbams.adapters.StudentAttenAdapter;
import com.example.lbams.databinding.ActivityAttendanceRecordBinding;
import com.example.lbams.model.MarkAttendanceModel;
import com.example.lbams.model.ScheduleModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AttendanceRecord extends BaseActvity {

    ActivityAttendanceRecordBinding binding;
    ArrayList<MarkAttendanceModel> record;
    ArrayList<ScheduleModel> scheData;
    String email;
    String sche;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAttendanceRecordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        record = new ArrayList<>();
        scheData = new ArrayList<>();
        Intent intent = getIntent();
        email = intent.getStringExtra("email");
        sche = intent.getStringExtra("sch");
        if(sche.equals("1")){
            binding.button2.setText("Find Class Schedule");
        }else{
            binding.button2.setText("Find Attendance Record");
        }
        binding.button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = binding.editText5.getText().toString();
                if(sche.equals("1")){
                    ScheData(code);
                }else{
                    getData(code);
                }

            }
        });


    }
    public void getData(String code){
        binding.progress.setVisibility(View.VISIBLE);
        dbRef.child("Attendance").child(code).child(email.replace(".",",")).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
               for(DataSnapshot snapshot1:snapshot.getChildren()){
                   MarkAttendanceModel model = snapshot1.getValue(MarkAttendanceModel.class);
                   assert model != null;
                   //Log.d("Dab",model.time);
                   record.add(model);
               }
                Collections.reverse(record);
               binding.progress.setVisibility(View.GONE);
                StudentAttenAdapter adapter = new StudentAttenAdapter(record);
                binding.recyclerView.setAdapter(adapter);
                binding.recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void ScheData(String code){
        binding.progress.setVisibility(View.VISIBLE);
        dbRef.child("Schedule").child(code).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                scheData.clear();
                for (DataSnapshot scheduleSnap : snapshot.getChildren()) {
                    ScheduleModel dataModel = scheduleSnap.getValue(ScheduleModel.class);
                    Log.d("DAB", dataModel.className);
                    scheData.add(dataModel);
                }
                binding.progress.setVisibility(View.GONE);
                ScheduleAdapter adapter = new ScheduleAdapter(scheData);
                binding.recyclerView.setAdapter(adapter);
                binding.recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}