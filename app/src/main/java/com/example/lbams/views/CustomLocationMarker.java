package com.example.lbams.views;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;

import com.example.lbams.R;
import com.example.lbams.databinding.ActivityCustomLocationMarkerBinding;
import com.example.lbams.model.AttendanceLocationList;
import com.example.lbams.model.CurrentAttLocation;
import com.example.lbams.model.MarkAttendanceModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CustomLocationMarker extends BaseActvity implements OnMapReadyCallback {

    ActivityCustomLocationMarkerBinding binding;
    GoogleMap map;
    LatLng oAttendanceArea;
    BitmapDescriptor schoolMarker;
    ArrayList<AttendanceLocationList> list;
    AttendanceLocationList sLocation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCustomLocationMarkerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.map.onCreate(savedInstanceState);
        binding.map.getMapAsync(this);

        schoolMarker = getBitmapDescriptor(this,R.drawable.ic_school);
        binding.saveLoc.setClickable(false);
        binding.saveLoc.setBackgroundColor(Color.GRAY);
         list  = new ArrayList<>();

    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
           map = googleMap;
           getLocation();
    }

    private void getLocation() {
        dbRef.child("MultiLocation").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snapshot1:snapshot.getChildren()){
                    AttendanceLocationList model = snapshot1.getValue(AttendanceLocationList.class);
                    assert model != null;
                    //Log.d("Dab",model.time);
                    showMap(model.lati, model.longi);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
}

    private void showMap(double lati, double longi) {
        LatLng location = new LatLng(lati, longi);
        if (map != null) {

            map.addMarker(new MarkerOptions().position(location).icon(schoolMarker).title("Welcome to School by local"));
            map.addCircle(new CircleOptions()
                    .center(location)
                    .radius(20)
                    .strokeWidth(2)
                    .strokeColor(Color.BLUE)
                    .fillColor(Color.argb(70, 0, 0, 255)));
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 19));
            changeLocation();
        }

    }

    public void changeLocation(){
        if(map != null){
            map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(@NonNull LatLng latLng) {
                    map.clear();
                    //list.clear();
                    Marker marker = map.addMarker(new MarkerOptions().position(latLng).icon(schoolMarker).title("New Location"));
                    map.addCircle(new CircleOptions()
                            .center(latLng)
                            .radius(20)
                            .strokeWidth(2)
                            .strokeColor(Color.BLUE)
                            .fillColor(Color.argb(70, 0, 0, 255)));
                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 19));
                    //list.add(sLocation);
                    saveMarkedLocation(marker);
                }
            });
        }
    }

    public void saveMarkedLocation(Marker marker){
        binding.saveLoc.setClickable(true);
        binding.saveLoc.setBackgroundColor(getResources().getColor(R.color.primaryTextColor));
        binding.saveLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.progress.setVisibility(View.VISIBLE);
                String ClassCode = binding.distance.getText().toString();
                AttendanceLocationList loc = new AttendanceLocationList(marker.getPosition().latitude, marker.getPosition().longitude,ClassCode );
                updateLocationToDB(loc);
            }
        });
    }

    private void updateLocationToDB(AttendanceLocationList loc) {
        Map<String, Object> updates = new HashMap<>();
        updates.put("MultiLocation", list);
        dbRef.child("MultiLocation").child(getRandomId()).setValue(loc, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                binding.progress.setVisibility(View.GONE);
                showCustomDialog("Location Marked Successfully");
            }
        });
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