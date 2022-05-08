package com.example.softwaredevproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RelativeLayout;

import java.util.ArrayList;

public class Activity3 extends AppCompatActivity{
    private String[] stopNameList = {
            "Gold Route", "Green Route", "Silver Route"
    };

    public ArrayList<Place> bList = createStopList(stopNameList);
    private ArrayAdapter<String> listViewAdapter;
    private AppAdapter appAdapter;
    RelativeLayout card;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_3);

        View inflatedView = getLayoutInflater().inflate(R.layout.list_item, null);
        card = (RelativeLayout)inflatedView.findViewById(R.id.cardSpace);

        EditText editText = findViewById(R.id.entryField);
        buildRecyclerView();
    }


    //creates a list of Place objects using stop names array
    //coordinates are set to null so that we may replace them with the nearest stop
    public static ArrayList<Place> createStopList(String[] nameList) {
        ArrayList<Place> stops = new ArrayList<Place>();
        for (int i = 0; i < nameList.length; i++) {
            stops.add(new Place(R.drawable.ic_place, nameList[i], null));
        }
        return stops;
    }

    private void buildRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.stopRecyclerView);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        appAdapter = new AppAdapter(bList);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(appAdapter);
    }
}