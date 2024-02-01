package com.example.lbams.views;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.util.Log;
import android.view.View;
import android.widget.Toast;


import com.example.lbams.R;
import com.example.lbams.databinding.ActivityAdminDashboardBinding;
import com.example.lbams.model.CurrentAttLocation;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

public class AdminDashboard extends BaseActvity {
    ActivityAdminDashboardBinding binding;
    private static final int REQUEST_LOCATION_PERMISSION = 1;
    private LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION_PERMISSION);
        }
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        binding.location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLocation();
            }
        });

        binding.addStduent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminDashboard.this, Registration.class);
                startActivity(intent);
            }
        });

        binding.addteacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminDashboard.this, Registration.class);
                startActivity(intent);
            }
        });

    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, initialize location manager and request updates
            } else {
                // Permission denied, handle accordingly
            }
        }
    }

    private void getLocation() {
        Toast.makeText(AdminDashboard.this, "Waiting for location ...", Toast.LENGTH_SHORT).show();
        binding.location.setClickable(false);
        binding.location.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                binding.location.setClickable(true);
                binding.location.setBackgroundColor(getResources().getColor(R.color.purple_200));
                double lati = location.getLatitude();
                double longi = location.getLongitude();
                updateLocationToDB(lati, longi);
            }
        };
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER, locationListener, null);

    }

    private void updateLocationToDB(double lati, double longi) {
        CurrentAttLocation location = new CurrentAttLocation(lati, longi);
        dbRef.child("Location").setValue(location, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                showCustomDialog("Location Marked Successfully");
            }
        });
           }


}