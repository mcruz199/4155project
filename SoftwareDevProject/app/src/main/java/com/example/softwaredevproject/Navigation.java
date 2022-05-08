package com.example.softwaredevproject;

import android.Manifest;
import android.app.Activity;
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

public class Navigation extends AppCompatActivity {
    private Context con;
    private Activity act;

    public Navigation(Context context, Activity activity) {
        con = context;
        act = activity;

    }

    public Activity getAct() {
        return act;
    }


    public List<String> readFile(String fileName) {
        List<String> coordList = new ArrayList<String>();
        try {
            Resources resources = this.con.getResources();
            int textFileId = resources.getIdentifier(fileName, "raw", this.con.getPackageName());
            InputStream is = resources.openRawResource(textFileId);

            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line = reader.readLine();

            while (line != null) {
                System.out.println(line);
                coordList.add(line);
                line = reader.readLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return coordList;
    }

    public void beginProximityNav(double lat, double lon, List<String> coords) {
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

        if (intent.resolveActivity(this.con.getPackageManager()) != null) {
            this.con.startActivity(intent);
        }
    }

    public void permissionsProtocol(LocationRequest locRequest, LocationManager locManager, List<String> placeCoordinates, Navigation navManager, boolean isEnabled) {
        Activity activity = navManager.getAct();
        FusedLocationProviderClient fusedLocClient;
        fusedLocClient = LocationServices.getFusedLocationProviderClient(activity);

            if (ActivityCompat.checkSelfPermission(this.con,
                    Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                //if the gps is turned on
                if (isEnabled == true) {
                    LocationServices.getFusedLocationProviderClient(activity)
                            .requestLocationUpdates(locRequest, new LocationCallback() {
                                @Override
                                public void onLocationResult(@NonNull LocationResult locationResult) {
                                    super.onLocationResult(locationResult);

                                    //stop location updates because only one location is needed
                                    LocationServices.getFusedLocationProviderClient(activity)
                                            .removeLocationUpdates(this);

                                    //get latitude and longitude of user location

                                    //TODO: add loading screen while the app is getting user coordinates
                                    if (locationResult != null && locationResult.getLocations().size() > 0) {
                                        int index = locationResult.getLocations().size() - 1;
                                        double userLat = locationResult.getLocations().get(index).getLatitude();
                                        double userLong = locationResult.getLocations().get(index).getLongitude();
                                        System.out.println(String.valueOf(userLat));
                                        System.out.println(String.valueOf(userLong));

                                        //call method to compare all phone coordinate sets and choose the nearest one for navigation
                                        navManager.beginProximityNav(userLat, userLong, placeCoordinates);
                                    }
                                }
                            }, Looper.getMainLooper());
                } else {
                    System.out.println("isEnable false");
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
                                            ResolvableApiException exception = (ResolvableApiException) e;
                                            exception.startResolutionForResult(activity, 2);
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
            }
        }


    }



