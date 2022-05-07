package com.example.softwaredevproject;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    LocationRequest locRequest;
    double userLat;
    double userLong;
    FusedLocationProviderClient fusedLocClient;
    List<String> phoneCoords = new ArrayList<String>();

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
        test = findViewById(R.id.textHello);
        fusedLocClient = LocationServices.getFusedLocationProviderClient(this);


        phoneCoords = readFile();
        System.out.println(phoneCoords);

        //on click, change activity
        buildingClickable.setOnClickListener(v -> openBuildingSearch());
        //on click, go to gps to get to phone
        phoneClickable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                locRequest = LocationRequest.create()
                        .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                        .setInterval(3000) //3000 milliseconds
                        .setFastestInterval(1000); //1000 milliseconds

                //ensures device has API level 23 or more, which allows for the use of methods like requestPermissions
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ActivityCompat.checkSelfPermission(MainActivity.this,
                            Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        LocationManager locManager = null;
                        boolean isEnabled = false;

                        //initialize locManager
                            locManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

                        //isEnabled true or false depending on whether or not GPS is turned on
                        isEnabled = locManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

                        //if the gps is turned on
                        if (isEnabled == true) {
                            LocationServices.getFusedLocationProviderClient(MainActivity.this)
                                    .requestLocationUpdates(locRequest, new LocationCallback() {
                                        @Override
                                        public void onLocationResult(@NonNull LocationResult locationResult) {
                                            super.onLocationResult(locationResult);

                                            //stop location updates because only one location is needed
                                            LocationServices.getFusedLocationProviderClient(MainActivity.this)
                                                    .removeLocationUpdates(this);

                                            //get latitude and longitude of user location

                                            //TODO: add loading screen while the app is getting user coordinates
                                            if (locationResult != null && locationResult.getLocations().size() > 0) {
                                                int index = locationResult.getLocations().size() - 1;
                                                userLat = locationResult.getLocations().get(index).getLatitude();
                                                userLong = locationResult.getLocations().get(index).getLongitude();
                                                System.out.println(String.valueOf(userLat));
                                                System.out.println(String.valueOf(userLong));

                                                //call method to compare all phone coordinate sets and choose the nearest one for navigation
                                                beginEPhoneNav(userLat, userLong, phoneCoords);
                                            }
                                        }
                                    }, Looper.getMainLooper());
                        } else {
                            //check if device location is enabled or not
                            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                                    .addLocationRequest(locRequest); //adds one LocationRequest that the client is interested in

                            //Whether or not location is required by the calling app in order to continue.
                            builder.setAlwaysShow(true);

                            //task
                            Task<LocationSettingsResponse> task = LocationServices.getSettingsClient(getApplicationContext())
                                    .checkLocationSettings(builder.build());

                            task.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {

                                @Override
                                public void onComplete(@NonNull Task<LocationSettingsResponse> task) {
                                    try {
                                        LocationSettingsResponse response = task.getResult(ApiException.class);

                                    } catch (ApiException e) {
                                        switch (e.getStatusCode()) {
                                            //when user device location is turned off, display popup asking for location permission
                                            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:

                                                //resolve error thrown by api exception class
                                                try {
                                                    ResolvableApiException exception = (ResolvableApiException)e;
                                                    exception.startResolutionForResult(MainActivity.this, 2);
                                                } catch (IntentSender.SendIntentException sendIntentException) {
                                                    sendIntentException.printStackTrace();
                                                }
                                                break;

                                                //catch for error if the device doesn't have a location
                                            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                                break;
                                        }
                                    }
                                }
                            });
                        }


                    } else {
                        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                    }
                }
            }
        });
    }

    //read phone coordinates text file line by line and assign each line to element in string list
    private List<String> readFile() {
        List<String> coordList = new ArrayList<String>();
        try {
            Resources resources = getResources();
            InputStream is = resources.openRawResource(R.raw.emergencyphonecoordinates);

            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line = reader.readLine();

            while (line != null) {
                coordList.add(line);
                line = reader.readLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return coordList;
    }

    //method that calculates closest emergency phone and sets course to it
    private void beginEPhoneNav(double lat, double lon, List<String> coords) {
        float[] result = new float[1];
        double currentEPhoneLat;
        double currentEPhoneLon;
        String bestCoords = "";
        float distance = 0.0f;
        float minDistance = 1000000000000.0f;

        for (int i = 0; i < coords.size(); i++) {
            String text = coords.get(i);
            String[] temp = text.split(",");
            currentEPhoneLat = Double.parseDouble(temp[0]);
            currentEPhoneLon = Double.parseDouble(temp[1]);

            Location.distanceBetween(lat, lon, currentEPhoneLat, currentEPhoneLon, result);
            distance = result[0];

            if (distance < minDistance) {
                minDistance = distance;
                bestCoords = coords.get(i);
            }
        }
        System.out.println(bestCoords);

        Intent intent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("google.navigation:q=" + bestCoords + "&mode=w"));
        intent.setPackage("com.google.android.apps.maps");

        if(intent.resolveActivity(MainActivity.this.getPackageManager()) != null) {
            MainActivity.this.startActivity(intent);

        }

    }

    //opens the building search activity
    public void openBuildingSearch(){
        Intent intent = new Intent(this, Activity2.class);
        startActivity(intent);
    }

}