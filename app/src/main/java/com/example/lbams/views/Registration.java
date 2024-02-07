package com.example.lbams.views;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.lbams.databinding.ActivityRegistrationBinding;
import com.example.lbams.model.User;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Registration extends BaseActvity {
    ActivityRegistrationBinding binding;

    User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegistrationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //getting Data
         addUserToFirebase();
    }
    void addUserToFirebase(){
        binding.submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fName  = binding.editTextFirstName.getText().toString();
                String lName = binding.editTextLastName.getText().toString();
                String email = binding.editTextEmail.getText().toString();
                String type = binding.spinnerType.getSelectedItem().toString();
                String userType= userType(type);

                user = new User(fName, lName, email, "123", userType, "","1");
                dbRef.child("User").child(email.replace(".", ",")).setValue(user);
            }
        });
    }
    String userType(String type){
        String userType = "";
        if(type.equalsIgnoreCase("Admin")){
            userType = "a";
        }else{
            if(type.equals("Teacher")){
                userType  = "t";
            }else{
                userType = "s";
            }
        }
      return  userType;
    }
}