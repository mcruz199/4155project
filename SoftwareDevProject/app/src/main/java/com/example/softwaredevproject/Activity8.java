package com.example.softwaredevproject;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

// Dorm list
public class Activity8 extends AppCompatActivity{
    private String[] dormNameList = {
            "Hunt Hall",
            "Scott Hall",
            "Laurel Hall",
            "Sanford Hall",
            "Levine Hall",
            "Elm Hall",
            "Oak Hall",
            "Maple Hall",
            "Cedar Hall",
            "Lynch Hall",
            "Belk Hall",
            "Wallis Hall",
            "Witherspoon Hall",
            "Miltimore Hall",
            "Martin Hall",
            "Hawthorn Hall"
    };

    private String[] dormCoordinates = {
            "35.30140452224128, -80.73631309524959",
            "35.30178978771136, -80.73540650860254",
            "35.302590958894804, -80.7360448743481",
            "35.30301255495081, -80.73361346918915",
            "35.30313404086676, -80.73287367832698",
            "35.30873035908972, -80.73111620903512",
            "35.30907704901271, -80.7322617603777",
            "35.30904299917537, -80.73115793441517",
            "35.30959416613318, -80.72897132644783",
            "35.310233359945535, -80.73370935711092",
            "35.31045200379119, -80.73463437070383",
            "35.310920524327585, -80.73394539506221",
            "35.31086326085204, -80.73230588821133",
            "35.311581654245735, -80.73556576370083",
            "35.31016920472146, -80.72740326853223",
            "35.31157557204089, -80.72742725602286"
    };

    public ArrayList<Place> dList = createPlaceList(dormNameList, dormCoordinates);
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

                for (Place dorm : dList) {
                    if (dorm.getName().toLowerCase().contains(s.toLowerCase())) {
                        filteredList.add(dorm);
                    }
                }
                appAdapter.filterList(filteredList);
            }

        });

    }


    //creates a list of Place objects using dorm names array
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
        appAdapter = new AppAdapter(dList);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(appAdapter);
    }
}