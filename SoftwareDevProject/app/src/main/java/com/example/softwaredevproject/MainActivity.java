package com.example.softwaredevproject;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
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
    LocationRequest locRequest;
    FusedLocationProviderClient fusedLocClient;
    List<String> phoneCoords = new ArrayList<String>();
    Navigation navManager = new Navigation(MainActivity.this, this);
    private static String ePhoneTextFileName = "emergencyphonecoordinates";

    LinearLayout buildingClickable;
    LinearLayout phoneClickable;
    LinearLayout foodClickable;
    LinearLayout stopClickable;
    TextView test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        buildingClickable = findViewById(R.id.layoutBuilding);
        phoneClickable = findViewById(R.id.layoutPhones);
        stopClickable = findViewById(R.id.layoutStops);
        test = findViewById(R.id.textHello);
        fusedLocClient = LocationServices.getFusedLocationProviderClient(this);


        phoneCoords = navManager.readFile(ePhoneTextFileName);

        //on click, change activity
        buildingClickable.setOnClickListener(v -> openBuildingSearch());
        stopClickable.setOnClickListener(v -> openStopSearch());


        phoneClickable.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                locRequest = LocationRequest.create()
                        .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                        .setInterval(3000) //3000 milliseconds
                        .setFastestInterval(1000); //1000 milliseconds

                //initialize locManager
                LocationManager locManager;

                //ensures device has API level 23 or more, which allows for the use of methods like requestPermissions
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ActivityCompat.checkSelfPermission(MainActivity.this,
                            Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        locManager = null;
                        boolean isEnabled = false;

                        //initialize locManager

                        locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                        //isEnabled true or false depending on whether or not GPS is turned on
                        isEnabled = locManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
                        navManager.permissionsProtocol(locRequest, locManager, phoneCoords, navManager, isEnabled);

                    } else {
                        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                    }
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



}