package com.example.lbams.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.lbams.R;
import com.example.lbams.adapters.ApproveAttendenceAdapter;
import com.example.lbams.adapters.StudentAttenAdapter;
import com.example.lbams.databinding.ActivityApproveAttendenceBinding;
import com.example.lbams.model.MarkAttendanceModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ApproveAttendence extends BaseActvity {
    ActivityApproveAttendenceBinding binding;
    ArrayList<MarkAttendanceModel> dataList;
    int counter = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityApproveAttendenceBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        String code = intent.getStringExtra("code");
        dataList = new ArrayList<>();
        //Toast.makeText(this, "Teacher Code "+ code, Toast.LENGTH_SHORT).show();
        getData(code);
    }

    public  void getData(String code){
        binding.progress.setVisibility(View.VISIBLE);
       dbRef.child("Attendance").child(code).addListenerForSingleValueEvent(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot snapshot) {
               ArrayList <String> emails = new ArrayList<>();
               for (DataSnapshot emailSnapshot : snapshot.getChildren()) {
                   String email = emailSnapshot.getKey();
                   emails.add(email);
               }
               getRecord(emails, code);
           }

           @Override
           public void onCancelled(@NonNull DatabaseError error) {

           }
       });
    }

    public void getRecord(ArrayList<String> list, String code){
        ArrayList<MarkAttendanceModel> AttData  = new ArrayList<>();
        int emails = list.size();

        for(String email:list){
            dbRef.child("Attendance").child(code).child(email).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    counter++;
                    for(DataSnapshot singleRec:snapshot.getChildren()){
                        MarkAttendanceModel model = singleRec.getValue(MarkAttendanceModel.class);
                        //Log.d("RecID", model.Id);
                        AttData.add(model);
;                    }
                    if(counter == emails){
                        ShowData(AttData);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


        }

    }

    private void ShowData(ArrayList<MarkAttendanceModel> dataList) {
        ArrayList<MarkAttendanceModel> refinedData  = new ArrayList<>();
        for (int i =0; i < dataList.size(); i++){
            if(dataList.get(i).isCheckedIn.equals("1") && dataList.get(i).isApproved.equals("0")){
                refinedData.add(dataList.get(i));
              /*  binding.progress.setVisibility(View.GONE);
                ApproveAttendenceAdapter adapter = new ApproveAttendenceAdapter(dataList);
                binding.recyclerView.setAdapter(adapter);
                binding.recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                break;*/}}

        if(refinedData.size() > 0 ){
            binding.progress.setVisibility(View.GONE);
            ApproveAttendenceAdapter adapter = new ApproveAttendenceAdapter(refinedData);
            binding.recyclerView.setAdapter(adapter);
            binding.recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        }else{
            binding.progress.setVisibility(View.GONE);
            binding.recyclerView.setVisibility(View.GONE);
            binding.notFound.setVisibility(View.VISIBLE);
        }
    }
}