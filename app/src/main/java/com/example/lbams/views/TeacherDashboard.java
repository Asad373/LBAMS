package com.example.lbams.views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.lbams.R;
import com.example.lbams.databinding.ActivityTeacherDashboardBinding;

public class TeacherDashboard extends AppCompatActivity {

    ActivityTeacherDashboardBinding binding;
    String code;
    String email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTeacherDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        email = intent.getStringExtra("email");
        code = intent.getStringExtra("code");
        binding.location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentAtt = new Intent(TeacherDashboard.this, ApproveAttendence.class);
                intentAtt.putExtra("code", code);
                startActivity(intentAtt);
            }
        });

        binding.lost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentAtt = new Intent(TeacherDashboard.this, TeacherSchedule.class);
                intentAtt.putExtra("code", code);
                startActivity(intentAtt);
            }
        });

        binding.Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(TeacherDashboard.this, Login.class);
                startActivity(intent1);
                finish();
            }
        });

    }
}