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

public class Activity4 extends AppCompatActivity{
    private String[] diningNameList = {
            "Aurum Cafe w/ Auntie Anne's", "Bojangles", "Chick-fil-A", "Crown2Go",
            "Crown Commons", "Fresh2U", "Fusion Cafe", "Mama Leone's",
            "Market on Craver", "Melt Lab", "Panda Express", "Peet's Cafe",
            "Rubi Coffee", "Salsarita's", "SoVi", "Starbuck's",
            "Shake Smart", "Subway", "Sushi with Gusto", "Thoughtful Cup",
            "Wendy's"
    };

    private String[] diningCoordinates = {
            "35.30827909748464, -80.7300797548494", "35.308664841379915, -80.73366310800392",
            "35.30713150326029, -80.7308241603034", "35.308664841379915, -80.73366310800392",
            "35.308664841379915, -80.73366310800392", "35.30713150326029, -80.7308241603034",
            "35.3116873254055, -80.74299595030368", "35.30713150326029, -80.7308241603034",
            "35.308664841379915, -80.73366310800392", "35.30713150326029, -80.7308241603034",
            "35.30509484819087, -80.73324453291707", "35.30574078405428, -80.73210911082026",
            "35.307576666820566, -80.7355821299574", "35.30713150326029, -80.7308241603034",
            "35.30275366756918, -80.73487966028404", "35.308664841379915, -80.73366310800392",
            "35.308664841379915, -80.73366310800392", "35.30509265927711, -80.73324587402159",
            "35.30713150326029, -80.7308241603034", "35.30587079993467, -80.72885715854241",
            "35.308664841379915, -80.73366310800392"

    };

    public ArrayList<Place> dList = createDiningList(diningNameList, diningCoordinates);
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

                for (Place building : dList) {
                    if (building.getName().toLowerCase().contains(s.toLowerCase())) {
                        filteredList.add(building);
                    }
                }
                appAdapter.filterList(filteredList);
            }

        });

    }


    //creates a list of Place objects using stop names array
    //coordinates are set to null so that we may replace them with the nearest stop
    public static ArrayList<Place> createDiningList(String[] nameList, String[] coordList) {
        ArrayList<Place> stops = new ArrayList<Place>();
        for (int i = 0; i < nameList.length; i++) {
            stops.add(new Place(R.drawable.ic_place, nameList[i], coordList[i]));
        }
        return stops;
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