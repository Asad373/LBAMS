package com.example.lbams.views;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.example.lbams.R;
import com.example.lbams.databinding.ActivityAccountRequestBinding;
import com.example.lbams.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class AccountRequest extends BaseActvity {

    ActivityAccountRequestBinding binding;
    boolean requestAccount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAccountRequestBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        requestAccount = false;
        binding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!requestAccount){
                    if(binding.editText2.getText().toString().equals("")){
                        showCustomDialog("Please enter email");
                    }else{
                        getRequest();
                    }
                }else{
                   if(binding.editText2.getText().toString().equals("")){
                       showCustomDialog("Please enter email");
                   }else{
                       if(binding.editText3.getText().toString().equals("")){
                           showCustomDialog("Please enter first name");
                       }else{
                           if(binding.editText4.getText().toString().equals("")){
                               showCustomDialog("Please enter last name");
                           }else{
                              addUserAccountRequest();
                           }
                       }
                   }
                }
            }
        });
    }
    public  void getRequest(){
            binding.progress.setVisibility(View.VISIBLE);
           String email = binding.editText2.getText().toString();
            dbRef.child("User").child(email.replace(".", ","))
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists()){
                                binding.progress.setVisibility(View.GONE);
                                User user = snapshot.getValue(User.class);
                                if(email.equals(user.Email)){
                                   binding.userRecord.setVisibility(View.VISIBLE);
                                   binding.userNameD.setText(user.Email);
                                   binding.pass.setText(user.pass);
                                }else{
                                    binding.progress.setVisibility(View.GONE);
                                    showCustomDialog("User not found, please contact admin");
                                }
                            }else{
                                binding.progress.setVisibility(View.GONE);
                                binding.userRecord.setVisibility(View.GONE);
                                binding.editText3.setVisibility(View.VISIBLE);
                                binding.editText4.setVisibility(View.VISIBLE);
                                binding.button.setText("Request for Account");
                                requestAccount = true;
                                //showCustomDialog("User not found, Please contact Admin!");
                            }


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
    }

    public void addUserAccountRequest(){
         String email = binding.editText2.getText().toString();
         String firstName = binding.editText3.getText().toString();
         String lastName = binding.editText4.getText().toString();
         binding.progress.setVisibility(View.VISIBLE);
         User user = new User(firstName, lastName, email,"","","","1");
         dbRef.child("AccountRequest").child(email.replace(".",",")).setValue(user, new DatabaseReference.CompletionListener() {
             @Override
             public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {

                 binding.editText2.setText("");
                 binding.editText3.setText("");
                 binding.editText4.setText("");
                 binding.progress.setVisibility(View.GONE);
                 showCustomDialog("Account request is submitted, \n please try to find account after 24 hours!");
             }
         });
    }
}