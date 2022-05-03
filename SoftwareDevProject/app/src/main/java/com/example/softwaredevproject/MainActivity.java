package com.example.softwaredevproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {
    LinearLayout buildingClickable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        buildingClickable = findViewById(R.id.layoutBuilding);
        //on click, change activity
        buildingClickable.setOnClickListener(v -> openBuildingSearch());


    }

    //opens the building search activity
    public void openBuildingSearch(){
        Intent intent = new Intent(this, Activity2.class);
        startActivity(intent);
    }
}