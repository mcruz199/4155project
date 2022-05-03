package com.example.softwaredevproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;

public class Activity2 extends AppCompatActivity{
    private String[] buildingNameList = {"Atkins Library", "Barnard", "Belk Gym", "Bioinformatics",
            "Burson", "Cameron Hall", "Cato College of Education", "Charlotte Engineering Early College",
            "College of Health and Human Services", "Colvard", "Denny", "Duke Centennial Hall", "EPIC",
            "Fretwell", "Friday", "Garinger", "Grigg Hall", "Jonson Band Center", "Kulwicki Laboratory",
            "Macy", "McEinry", "Memorial Hall", "Motorsports Research", "PORTAL", "Robinson Hall",
            "Rowe", "Science Building", "Smith", "Storrs", "Student Union", "Winningham", "Woodward"};

    public ArrayList<Place> bList = createPlaceList(buildingNameList);
    private ArrayAdapter<String> listViewAdapter;
    private AppAdapter appAdapter;
    RelativeLayout card;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2);

        View inflatedView = getLayoutInflater().inflate(R.layout.list_item, null);
        card = (RelativeLayout)inflatedView.findViewById(R.id.cardSpace);

        EditText editText = findViewById(R.id.entryField);
        buildRecyclerView();

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String s = editable.toString();
                ArrayList<Place> filteredList = new ArrayList<Place>();

                for (Place building : bList) {
                    if (building.getName().toLowerCase().contains(s.toLowerCase())) {
                        filteredList.add(building);
                    }
                }
                appAdapter.filterList(filteredList);
            }

        });

    }

    //creates a list of Building objects using building names array
    public static ArrayList<Place> createPlaceList(String[] list) {
        ArrayList<Place> places = new ArrayList<Place>();

        for (int i = 0; i < list.length; i++) {
            places.add(new Place(R.drawable.ic_place, list[i]));
        }
        return places;
    }

    private void buildRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.placeRecyclerView);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        appAdapter = new AppAdapter(bList);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(appAdapter);
    }
}