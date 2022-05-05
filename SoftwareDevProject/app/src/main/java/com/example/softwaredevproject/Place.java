package com.example.softwaredevproject;

import java.util.ArrayList;

//place class
public class Place {
    private int icon;
    private String name;
    private String coords;

    //constructor for place class
    public Place(int imageResource, String pName, String latAndLon) {
        icon = imageResource;
        name = pName;
        coords = latAndLon;
    }

    //name getter
    public String getName() {
        return name;
    }

    //icon getter
    public int getIcon() {
        return icon;
    }

    //coordinateGetter

    public String getCoordinates() {
        return coords;
    }
}