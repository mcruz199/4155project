package com.example.softwaredevproject;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    LocationRequest locRequest = LocationRequest.create()
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
            .setInterval(3000) //3000 milliseconds
            .setFastestInterval(1000); //1000 milliseconds

    LocationManager locManager = null;
    boolean isEnabled = false;
    FusedLocationProviderClient fusedLocClient;
    private static String ePhoneTextFileName = "emergencyphonecoordinates";


    LinearLayout buildingClickable;
    LinearLayout phoneClickable;
    LinearLayout foodClickable;
    LinearLayout stopClickable;
    LinearLayout amenityClickable;
    LinearLayout reviewClickable;
    LinearLayout favoriteClickable;
    LinearLayout dormClickable;
    TextView test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Navigation navManager = new Navigation(this, this);
        List<String> phoneCoords = phoneCoords = navManager.readFile(ePhoneTextFileName);


        buildingClickable = findViewById(R.id.layoutBuilding);
        phoneClickable = findViewById(R.id.layoutPhones);
        stopClickable = findViewById(R.id.layoutStops);
        foodClickable = findViewById(R.id.layoutFood);
        amenityClickable = findViewById(R.id.layoutAmenity);
        reviewClickable = findViewById(R.id.layoutReview);
        favoriteClickable = findViewById(R.id.layoutFavorite);
        dormClickable = findViewById(R.id.layoutDorm);

        test = findViewById(R.id.textHello);
        fusedLocClient = LocationServices.getFusedLocationProviderClient(this);


        phoneCoords = navManager.readFile(ePhoneTextFileName);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(MainActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locManager = (LocationManager) getSystemService(MainActivity.this.LOCATION_SERVICE);
                //isEnabled true or false depending on whether or not GPS is turned on
                isEnabled = locManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            } else {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        }

        //on click, change activity
        buildingClickable.setOnClickListener(v -> openBuildingSearch());
        stopClickable.setOnClickListener(v -> openStopSearch());
        foodClickable.setOnClickListener(v -> openFoodSearch());
        favoriteClickable.setOnClickListener(v -> openFavoriteList());
        amenityClickable.setOnClickListener(v -> openAmenitySearch());
        reviewClickable.setOnClickListener(v -> openLeaveReview());
        dormClickable.setOnClickListener(v -> openDormList());


        List<String> finalPhoneCoords = phoneCoords;
        phoneClickable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ActivityCompat.checkSelfPermission(MainActivity.this,
                        Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    locManager = (LocationManager) getSystemService(MainActivity.this.LOCATION_SERVICE);
                    isEnabled = locManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
                    navManager.permissionsProtocol(locRequest, locManager, finalPhoneCoords, navManager, isEnabled);
                }
            }
        });

    }

    //opens the building search activity
    public void openBuildingSearch(){
        Intent intent = new Intent(this, Activity2.class);
        startActivity(intent);
    }

    //opens the building search activity
    public void openStopSearch() {
        Intent intent = new Intent(this, Activity3.class);
        startActivity(intent);
    }

    //opens the food search activity
    public void openFoodSearch() {
        Intent intent = new Intent(this, Activity4.class);
        startActivity(intent);
    }

    //opens the amenity search activity
    public void openAmenitySearch() {
        Intent intent = new Intent(this, Activity5.class);
        startActivity(intent);
    }

    //opens the review form
    public void openLeaveReview() {
        Uri uri = Uri.parse("https://docs.google.com/forms/d/e/1FAIpQLSfy-o69RyJc3l1NZmUqiLE2mjDE6JrvniaxA3wR4Vz4E-PQHQ/viewform?usp=sf_link"); // missing 'http://' will cause crashed
        startActivity(new Intent(Intent.ACTION_VIEW, uri));
    }

    //opens the favorites list
    public void openFavoriteList() {
        Intent intent = new Intent(this, Activity6.class);
        startActivity(intent);
    }

    //opens the dorms list
    public void openDormList() {
        Intent intent = new Intent(this, Activity8.class);
        startActivity(intent);
    }

}