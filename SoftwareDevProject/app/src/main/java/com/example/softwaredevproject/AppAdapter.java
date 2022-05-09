package com.example.softwaredevproject;
import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;


import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class AppAdapter extends RecyclerView.Adapter<AppAdapter.appViewHolder> {
    private ArrayList<Place> placeList;
    private List<String> stopCoords = new ArrayList<String>();
    private static String silverRouteTextFileName = "silverroutestoplocations";
    private static String greenRouteTextFileName = "greenroutestoplocations";
    private static String goldRouteTextFileName = "goldroutestoplocations";
    RelativeLayout card;
    String destinationCoords;

    public static class appViewHolder extends RecyclerView.ViewHolder {
        public ImageView icon;
        public TextView placeName;

        public appViewHolder(View itemView) {
            super(itemView);
            placeName = itemView.findViewById(R.id.textView);
            icon = itemView.findViewById(R.id.imageView);
        }
    }

    public AppAdapter(ArrayList<Place> list) {
        placeList = list;
    }

    @Override
    public appViewHolder onCreateViewHolder(ViewGroup group, int viewType) {
        View view = LayoutInflater.from(group.getContext()).inflate(R.layout.list_item, group, false);
        card = (RelativeLayout)view.findViewById(R.id.cardSpace);
        FusedLocationProviderClient fusedLocClient;
        fusedLocClient = LocationServices.getFusedLocationProviderClient((Activity)group.getContext());

        card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView textView = (TextView) view.findViewById(R.id.textView);
                String text = textView.getText().toString();
                Navigation navManager = new Navigation(group.getContext(), (Activity) group.getContext());

                for (Place building : placeList) {
                    if (building.getName().toLowerCase().contains(text.toLowerCase())) {
                        if (building.getName().toLowerCase().contains("silver route")
                                || building.getName().toLowerCase().contains("green route")
                                || building.getName().toLowerCase().contains("gold route")) {

                            if (text == "Silver Route") {
                                stopCoords = navManager.readFile(silverRouteTextFileName);
                            }

                            if (text == "Green Route") {
                                stopCoords = navManager.readFile(greenRouteTextFileName);
                            }

                            if (text == "Gold Route") {
                                stopCoords = navManager.readFile(goldRouteTextFileName);
                            }
                            System.out.println("match");


                            LocationRequest locRequest;

                            locRequest = LocationRequest.create()
                                    .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                                    .setInterval(3000) //3000 milliseconds
                                    .setFastestInterval(1000); //1000 milliseconds
                            LocationManager locManager;


                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                if (ActivityCompat.checkSelfPermission(group.getContext(),
                                        Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                                    locManager = null;
                                    boolean isEnabled = false;

                                    //initialize locManager

                                    locManager = (LocationManager) group.getContext().getSystemService(group.getContext().LOCATION_SERVICE);
                                    //isEnabled true or false depending on whether or not GPS is turned on
                                    isEnabled = locManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
                                    navManager.permissionsProtocol(locRequest, locManager, stopCoords, navManager, isEnabled);

                                }
                            }

                        } else {
                            destinationCoords = building.getCoordinates();
                            Intent intent = new Intent(Intent.ACTION_VIEW,
                                    Uri.parse("google.navigation:q=" + destinationCoords + "&mode=w"));
                            intent.setPackage("com.google.android.apps.maps");
                            System.out.println("match");

                            if (intent.resolveActivity(group.getContext().getPackageManager()) != null) {
                                group.getContext().startActivity(intent);
                            }
                        }

                    }


                    }
                }
        });


        appViewHolder a = new appViewHolder(view);
        return a;
    }

    //tells app that the suggestions have been narrowed and the list on screen must be reduced
    public void filterList(ArrayList<Place> filteredList) {
        placeList = filteredList;
        notifyDataSetChanged();
    }

    //attaches building names and icons to view
    @Override
    public void onBindViewHolder(appViewHolder holder, int index) {
        Place currentPlace = placeList.get(index);
        holder.icon.setImageResource(currentPlace.getIcon());
        holder.placeName.setText(currentPlace.getName());
    }

    @Override
    public int getItemCount() {
        return placeList.size();
    }


}