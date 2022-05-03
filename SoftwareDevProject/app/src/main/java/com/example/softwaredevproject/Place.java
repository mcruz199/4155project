package com.example.softwaredevproject;

//place class
public class Place {
    private int icon;
    private String name;

    //constructor for place class
    public Place(int imageResource, String pName) {
        icon = imageResource;
        name = pName;
    }

    //name getter
    public String getName() {
        return name;
    }

    //icon getter
    public int getIcon() {
        return icon;
    }
}