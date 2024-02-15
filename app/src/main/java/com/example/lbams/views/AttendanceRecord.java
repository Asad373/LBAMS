package com.example.lbams.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.lbams.R;
import com.example.lbams.adapters.StudentAttenAdapter;
import com.example.lbams.databinding.ActivityAttendanceRecordBinding;
import com.example.lbams.model.MarkAttendanceModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AttendanceRecord extends BaseActvity {

    ActivityAttendanceRecordBinding binding;
    ArrayList<MarkAttendanceModel> record;
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAttendanceRecordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        record = new ArrayList<>();
        Intent intent = getIntent();
        email = intent.getStringExtra("email");
        binding.button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = binding.editText5.getText().toString();
                getData(code);
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
}