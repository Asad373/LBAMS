package com.example.lbams.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;

import com.example.lbams.databinding.ActivityRegistrationBinding;
import com.example.lbams.model.AttendanceLocationList;
import com.example.lbams.model.User;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Registration extends BaseActvity {
    ActivityRegistrationBinding binding;
    ArrayList<String> classList;
    User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegistrationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //getting Data
        getLocation();
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
                String code  = binding.spinnerClass.getSelectedItem().toString();
                String pass  = binding.editTextPassword.getText().toString();
                String userType= userType(type);

                user = new User(fName, lName, email, pass, userType, code,"1");
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
}