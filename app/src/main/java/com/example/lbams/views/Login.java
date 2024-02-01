package com.example.lbams.views;

import androidx.annotation.LongDef;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.hardware.usb.UsbRequest;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.lbams.R;
import com.example.lbams.databinding.ActivityLoginBinding;
import com.example.lbams.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class Login extends BaseActvity {
    ActivityLoginBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate();
            }
        });
    }

    private void validate(){
        if(binding.editText.getText().toString().equalsIgnoreCase("")){
            showCustomDialog("Please add valid email");
        }else{
            if(binding.pass.getText().toString().equalsIgnoreCase("")){
                showCustomDialog("Please add valid password");
            }else{
               firebaseDBcall();
            }
        }
    }

    private void firebaseDBcall(){
       String email = binding.editText.getText().toString();
       String password = binding.pass.getText().toString();
       dbRef.child("User").child(email.replace(".", ","))
       .addListenerForSingleValueEvent(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot snapshot) {
               if(snapshot.exists()){
                   User yourModel = snapshot.getValue(User.class);
                   if(email.equals(yourModel.Email)){
                       if(yourModel.pass.equals(password)){
                         NavigateUser(yourModel);
                       }else{
                           showCustomDialog("email or password is wrong");
                       }
                   }else{
                       showCustomDialog("User not found, please contact admin");
                   }
               }else{
                   showCustomDialog("User not found");
               }


           }

           @Override
           public void onCancelled(@NonNull DatabaseError error) {

           }
       });
    }

    private void NavigateUser(User user){
          if(user.Role.equals("a")){
              Intent intent  = new Intent(Login.this, AdminDashboard.class);
              startActivity(intent);
          }else{
              if(user.Role.equals("t")){
                  Intent intent  = new Intent(Login.this, TeacherDashboard.class);
                  startActivity(intent);
              }else{
                  if(user.Role.equals("s")){
                      Intent intent  = new Intent(Login.this, StudentDashboard.class);
                      startActivity(intent);
                  }
              }
          }
    }
}

