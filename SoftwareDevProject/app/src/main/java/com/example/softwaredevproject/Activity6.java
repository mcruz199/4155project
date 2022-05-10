package com.example.softwaredevproject;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

// Favorites page. Will show all favorites saved by user
public class Activity6 extends AppCompatActivity{


    public String[] favoriteNameList = {
            "Student Union"
};

    private String[] favoriteCoordinates = {
        "35.30867904610401, -80.73366522465017"

    };

    public ArrayList<Place> fList = createPlaceList(favoriteNameList, favoriteCoordinates);
    private ArrayAdapter<String> listViewAdapter;
    private AppAdapter appAdapter;
    RelativeLayout card;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_6);

        // Event listener for button. Will launch activity 7 which contains all places
        Button addFavoriteButton = findViewById(R.id.addFavorite);
        addFavoriteButton.setOnClickListener(v -> addFavorite());

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

                for (Place favorite : fList) {
                    if (favorite.getName().toLowerCase().contains(s.toLowerCase())) {
                        filteredList.add(favorite);
                    }
                }
                appAdapter.filterList(filteredList);
            }

        });

    }


    //creates a list of Place objects using building names array
    public static ArrayList<Place> createPlaceList(String[] nameList, String[] coordList) {
        ArrayList<Place> places = new ArrayList<Place>();

        for (int i = 0; i < nameList.length; i++) {
            places.add(new Place(R.drawable.ic_place, nameList[i], coordList[i]));
            System.out.println(places.get(i).getName());
            System.out.println(places.get(i).getCoordinates());
        }
        return places;
    }

    private void buildRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.placeRecyclerView);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        appAdapter = new AppAdapter(fList);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(appAdapter);
    }


    //opens the review form
    public void addFavorite() {
        Intent intent = new Intent(this, Activity7.class);
        startActivity(intent);
    }

    public String[] favorites() {
        String output = "";
        try {
            InputStream is = getAssets().open("favorites.txt");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            output = new String(buffer);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        String lines[] = output.split("\\r?\\n");
        return lines;
    }

    }