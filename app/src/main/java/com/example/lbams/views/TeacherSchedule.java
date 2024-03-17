package com.example.lbams.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.lbams.R;
import com.example.lbams.adapters.ScheduleAdapter;
import com.example.lbams.adapters.StudentAttenAdapter;
import com.example.lbams.databinding.ActivityTeacherScheduleBinding;
import com.example.lbams.model.MarkAttendanceModel;
import com.example.lbams.model.ScheduleModel;
import com.example.lbams.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class TeacherSchedule extends BaseActvity {
    ActivityTeacherScheduleBinding binding;
    ArrayList<ScheduleModel> dataList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTeacherScheduleBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        String code = intent.getStringExtra("code");
        dataList = new ArrayList<>();
        getData(code);
    }

    public  void getData(String code){
        binding.progress.setVisibility(View.VISIBLE);
        dbRef.child("Schedule").child(code).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot scheduleSnap : snapshot.getChildren()) {
                    ScheduleModel dataModel = scheduleSnap.getValue(ScheduleModel.class);
                    Log.d("DAB", dataModel.className);
                    dataList.add(dataModel);
                }
               binding.progress.setVisibility(View.GONE);
                ScheduleAdapter adapter = new ScheduleAdapter(dataList);
                binding.recyclerView.setAdapter(adapter);
                binding.recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}