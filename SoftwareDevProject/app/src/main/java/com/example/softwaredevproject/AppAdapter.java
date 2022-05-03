package com.example.softwaredevproject;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class AppAdapter extends RecyclerView.Adapter<AppAdapter.appViewHolder> {
    private ArrayList<Place> placeList;
    RelativeLayout card;

    public static class appViewHolder extends RecyclerView.ViewHolder {
        public ImageView icon;
        public TextView placeName;

        public appViewHolder(View itemView) {
            super(itemView);
            placeName = itemView.findViewById(R.id.textView);
            icon = itemView.findViewById(R.id.imageView);
        }
    }

    public AppAdapter(ArrayList<Place> list) {
        placeList = list;
    }

    @Override
    public appViewHolder onCreateViewHolder(ViewGroup group, int viewType) {
        View view = LayoutInflater.from(group.getContext()).inflate(R.layout.list_item, group, false);
        card = (RelativeLayout)view.findViewById(R.id.cardSpace);

        card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("google.navigation:q=35.3062,-80.7290&mode=w"));
                intent.setPackage("com.google.android.apps.maps");

                if(intent.resolveActivity(group.getContext().getPackageManager()) != null) {
                    group.getContext().startActivity(intent);
                }
            }
        });

        appViewHolder a = new appViewHolder(view);
        return a;
    }

    public void filterList(ArrayList<Place> filteredList) {
        placeList = filteredList;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(appViewHolder holder, int index) {
        Place currentPlace = placeList.get(index);
        holder.icon.setImageResource(currentPlace.getIcon());
        holder.placeName.setText(currentPlace.getName());
    }

    @Override
    public int getItemCount() {
        return placeList.size();
    }
}