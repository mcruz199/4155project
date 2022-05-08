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

    private String[] buildingCoords = {
            "35.30574078405428, -80.73210911082026", "35.30581832239878, -80.72982582646615",
            "35.305016567402944, -80.73557254917655", "35.312729256354814, -80.74191618086718", "35.30749610119972, -80.73244550601433",
            "35.30768351658367, -80.73122703997495", "35.307589808940804, -80.73406587467343", "35.30892656304262, -80.74405622753297",
            "35.30717130676656, -80.73330993701222", "35.30447505232191, -80.73172931746137", "35.305406827142576, -80.72961649595288",
            "35.312101531659515, -80.74142813345306", "35.30918568443445, -80.741492618056", "35.30587079993467, -80.72885715854241",
            "35.30631334029901, -80.7299323393359", "35.30500451909816, -80.72981189318254", "35.31142631144587, -80.74220321150668",
            "35.30391624386102, -80.72882127551144", "35.31229131281622, -80.74071329991746", "35.30572551514412, -80.73047925173054",
            "35.307240217471005, -80.73020566641168", "35.30388905472804, -80.73585417565862", "35.31251097031217, -80.74039012080615",
            "35.3116873254055, -80.74299595030368", "35.3033660318889, -80.72993358496366", "35.30463519126367, -80.7307577193025",
            "35.30827909748464, -80.7300797548494", "35.30682336728043, -80.73130228164028", "35.304599587893, -80.72912843729297",
            "35.308664841379915, -80.73366310800392", "35.30513019821512, -80.73040272689924", "35.307576666820566, -80.7355821299574"
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