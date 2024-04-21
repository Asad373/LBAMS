
package com.example.lbams.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.lbams.R;
import com.example.lbams.adapters.ApproveAttendenceAdapter;
import com.example.lbams.adapters.RequestedAccountAdapter;
import com.example.lbams.databinding.ActivityRequestedAccountBinding;
import com.example.lbams.model.MarkAttendanceModel;
import com.example.lbams.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RequestedAccount extends BaseActvity {

     ActivityRequestedAccountBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRequestedAccountBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getData();
    }

    public  void getData(){
        binding.progress.setVisibility(View.VISIBLE);
        dbRef.child("AccountRequest").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.getValue() != null){
                    ArrayList<User> userData = new ArrayList<>();
                    for (DataSnapshot user : snapshot.getChildren()) {
                        User model = user.getValue(User.class);
                        //Log.d("RecID", model.Id);
                        userData.add(model);
                    }
                    populateData(userData);
                }else{
                    binding.progress.setVisibility(View.GONE);
                    binding.recyclerView.setVisibility(View.GONE);
                    binding.notFound.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void populateData(ArrayList<User> data){
        binding.progress.setVisibility(View.GONE);
        RequestedAccountAdapter adapter = new RequestedAccountAdapter(data);
        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
    }
}