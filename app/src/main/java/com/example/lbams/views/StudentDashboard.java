package com.example.lbams.views;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import com.example.lbams.R;
import com.example.lbams.databinding.ActivityStudentDashboardBinding;
import com.example.lbams.model.CurrentAttLocation;
import com.example.lbams.model.MarkAttendanceModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class StudentDashboard extends BaseActvity implements OnMapReadyCallback {

    ActivityStudentDashboardBinding binding;
    private GoogleMap googleMap;
    SupportMapFragment mapFragment;
    Handler mMapHandler;
    LatLng oAttendanceArea;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;
    LocationManager mLocationManager;
    BitmapDescriptor schoolMarker;
    BitmapDescriptor studentMarker;
    boolean isCheckedInSave;
    boolean isCheckedOutSave;
    String email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStudentDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Intent receivedIntent = getIntent();
        email = receivedIntent.getStringExtra("email");
         isCheckedInSave = false;
         isCheckedOutSave = false;
         schoolMarker = getBitmapDescriptor(this,R.drawable.ic_school);
         studentMarker = getBitmapDescriptor(this, R.drawable.ic_student);

        askLocationPermission();
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mMapHandler = new Handler();
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        gotoDetail();
        signOut();
    }
    public void signOut(){
      binding.logout.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
               Intent intent = new Intent(StudentDashboard.this, Login.class);
               startActivity(intent);
               finish();
          }
      });
    }
    public void gotoDetail(){
        binding.detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              Intent intent = new Intent(StudentDashboard.this, AttendanceRecord.class);
              intent.putExtra("email", email);
              startActivity(intent);
            }
        });
    }
    private void getLocation() {
        dbRef.child("Location").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                CurrentAttLocation location = snapshot.getValue(CurrentAttLocation.class);
                if (location != null) {
                    oAttendanceArea = new LatLng(location.lati, location.longi);
                    showMap(location.lati, location.longi);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void showMap(double lati, double longi) {
        LatLng location = new LatLng(lati, longi);
        if (googleMap != null) {
            updateAttendanceOnMap();
            googleMap.addMarker(new MarkerOptions().position(location).icon(schoolMarker).title("Welcome to School by local"));
            googleMap.addCircle(new CircleOptions()
                    .center(location)
                    .radius(30)
                    .strokeWidth(2)
                    .strokeColor(Color.BLUE)
                    .fillColor(Color.argb(70, 0, 0, 255)));
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 19));
            mMapHandler.postDelayed(runnable, 10000);
        }

    }

    @Override
    public void onMapReady(@NonNull GoogleMap map) {
        googleMap = map;
        getLocation();
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (googleMap != null) {
                updateAttendanceOnMap();
            }
            mMapHandler.postDelayed(this, 10000);
        }
    };

    public void updateAttendanceOnMap() {

        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        String provider = mLocationManager.getBestProvider(criteria, true);

        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {

                float[] distanceCovered  = new float[1];
                Log.d("locationAccu", "Location Update - Provider: " + location.getProvider());
                Log.d("locationAccu", "Latitude: " + location.getLatitude() + ", Longitude: " + location.getLongitude());
                Log.d("locationAccu", "Accuracy: " + location.getAccuracy() + " meters");

                Location.distanceBetween(oAttendanceArea.latitude, oAttendanceArea.longitude, location.getLatitude(), location.getLongitude(), distanceCovered);
                Log.d("Distance", String.valueOf(distanceCovered[0]));

                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

                if(googleMap != null ){
                    if(distanceCovered[0] < 30){
                        googleMap.clear();
                        AttendanceArea(oAttendanceArea,  true);
                        googleMap.addMarker(new MarkerOptions().position(latLng).icon(studentMarker).title("you are here"));
                        saveAttendance(distanceCovered[0], true);
                    }else{
                        googleMap.clear();
                        AttendanceArea(oAttendanceArea,  false);
                        googleMap.addMarker(new MarkerOptions().position(latLng).icon(studentMarker).title("you are here"));
                        saveAttendance(distanceCovered[0] ,false);
                    }
                }
                mLocationManager.removeUpdates(this);
            }
        };

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
           askLocationPermission();
            return;
        }

        mLocationManager.requestLocationUpdates(provider, 0, 0, locationListener);

    }
    public void askLocationPermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                // Request location permission if not granted
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        LOCATION_PERMISSION_REQUEST_CODE);
            } else {
                // Location permission already granted, proceed with your logic
                // e.g., start location updates or use the device's location
                // Your code here
            }
        } else {
            // Location permission is not required for devices running below Android 6.0
            // Proceed with your logic
            // Your code here
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            // Check if the location permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Location permission granted, proceed with your logic
                // Your code here
            } else {
                // Location permission denied
                // Handle the situation when the user denies location permission
            }
        }
    }

    public  void AttendanceArea(LatLng attenArea, boolean CheckedIn){
        if(CheckedIn){
            googleMap.addMarker(new MarkerOptions().position(attenArea).icon(schoolMarker).title("Welcome to School"));
            googleMap.addCircle(new CircleOptions()
                    .center(attenArea)
                    .radius(30)
                    .strokeWidth(2)
                    .strokeColor(Color.GREEN)
                    .fillColor(Color.argb(70, 0, 255, 0)));
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(attenArea, 19));
        }else{
            googleMap.addMarker(new MarkerOptions().position(attenArea).icon(schoolMarker).title("Welcome to School"));
            googleMap.addCircle(new CircleOptions()
                    .center(attenArea)
                    .radius(30)
                    .strokeWidth(2)
                    .strokeColor(Color.RED)
                    .fillColor(Color.argb(70, 255, 0, 0)));
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(attenArea, 19));
        }

    }

    public void saveAttendance(float distance, boolean CheckedIn) {
        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String formattedDate = dateFormat.format(currentDate);
        if (CheckedIn) {
            if(!isCheckedInSave){
                binding.attenStatus.setText("Checked In");
                binding.Date.setText(formattedDate);
                binding.time.setText(getTime());
                binding.attenStatus.setTextColor(Color.GREEN);
                isCheckedInSave = true;
                isCheckedOutSave = false;
                addRecordToDb(true, formattedDate, getTime());

            }

        } else {
            if(!isCheckedOutSave){
                binding.attenStatus.setText("Checked Out");
                binding.Date.setText(formattedDate);
                binding.time.setText(getTime());
                binding.attenStatus.setTextColor(Color.RED);
                isCheckedOutSave = true;
                isCheckedInSave = false;
                addRecordToDb(false, formattedDate, getTime());
            }

        }

    }

    void addRecordToDb(boolean checkIn, String date, String time){
        if(checkIn){
            MarkAttendanceModel model = new MarkAttendanceModel("1","",date, time);
            dbRef.child("Attendance").child(email.replace(".", ",")).child(getRandomId()).setValue(model);
        }else{
            MarkAttendanceModel model = new MarkAttendanceModel("","1",date, time);
            dbRef.child("Attendance").child(email.replace(".", ",")).child(getRandomId()).setValue(model);
        }

    }


     String getTime(){
        Date currentTime = new Date();
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss a");
        String formattedTime = timeFormat.format(currentTime);
        return formattedTime;
    }

    public  BitmapDescriptor getBitmapDescriptor(Context context, int vectorDrawableResourceId) {
        // Get the Drawable from the vector resource
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorDrawableResourceId);

        // Create a Bitmap from the Drawable
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(),
                vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        vectorDrawable.draw(canvas);

        // Create a BitmapDescriptor from the Bitmap
        return BitmapDescriptorFactory.fromBitmap(bitmap);
}

    private  String getRandomId() {
        Date currentTime = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("HHmmss");
        String formattedTime = sdf.format(currentTime);
       return formattedTime;
    }
}