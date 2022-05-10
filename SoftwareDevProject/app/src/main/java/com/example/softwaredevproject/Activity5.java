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

// Amenities list
public class Activity5 extends AppCompatActivity{
    private String[] amenityNameList = {
            "UNCC Botanical Gardens",
            "Brocker Pond",
            "Hechenbleikner Lake",
            "Archaeopteryx Pond",
            "Halton-Wagner Tennis Complex",
            "Irwin Belk Track and Field Center",
            "Sue M. Daughtridge Stadium",
            "Robert & Mariam Hayes Stadium",
            "Jerry Richardson Stadium",
            "Van Landingham Glen Park",
            "Mellichamp Native Terrace/Garden"
    };

    private String[] amenityCoordinates = {
            "35.307867951750715, -80.72978866150191",
            "35.303681409345955, -80.73510266157038",
            "35.303852861960735, -80.731468548948",
            "35.3082601822158, -80.74460745870644",
            "35.30726374278626, -80.73739569753765",
            "35.30558430601749, -80.73808723260562",
            "35.30673585088609, -80.73958934618435",
            "35.30796167098843, -80.73878518433538",
            "35.31058040621876, -80.74029488434012",
            "35.30683094036692, -80.72793611699026",
            "35.308974799614255, -80.728374157672"

    };

    public ArrayList<Place> bList = createPlaceList(amenityNameList, amenityCoordinates);
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

                for (Place amenity : bList) {
                    if (amenity.getName().toLowerCase().contains(s.toLowerCase())) {
                        filteredList.add(amenity);
                    }
                }
                appAdapter.filterList(filteredList);
            }

        });

    }


    //creates a list of Place objects using amenity names array
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
        appAdapter = new AppAdapter(bList);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(appAdapter);
    }
}