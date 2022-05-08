package com.example.softwaredevproject;

//place class
public class Place {
    private int icon;
    private String name;
    private String coords;
    private int type;

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

    public void setCoords(String latAndLon) {
        this.coords = latAndLon;
    }
}