package com.example.lbams.views;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.lbams.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

abstract class BaseActvity extends AppCompatActivity {
    FirebaseDatabase database;
    DatabaseReference dbRef;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        database = FirebaseDatabase.getInstance();
        dbRef = database.getReference();
    }

    public void showCustomDialog(String mesg) {
        // Create a LayoutInflater to inflate the custom layout
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.layout_dialoge, null);

        // Reference the UI elements in the custom layout
        TextView dialogMessage = dialogView.findViewById(R.id.message);
        dialogMessage.setText(mesg);

        // Create the AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);

        // Set any additional properties for the dialog, if needed


        // Set actions for the OK button
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Handle OK button click
                // Do something with the user input
                dialog.dismiss();
            }
        });

        // Set actions for the Cancel button (if needed)


        // Create and show the AlertDialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
