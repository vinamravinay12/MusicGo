package com.example.vinam.musicgo.adapters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.vinam.musicgo.R;
import com.example.vinam.musicgo.activities.PlaylistsActivity;
import com.example.vinam.musicgo.fragments.SpotifyPlayerFragment;
import com.example.vinam.musicgo.fragments.StationsFragment;
import com.example.vinam.musicgo.holders.StationViewHolder;
import com.example.vinam.musicgo.model.Stations;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vinam on 11/6/2016.
 */

public class StationsAdapter extends RecyclerView.Adapter<StationViewHolder> {

    private ArrayList<Stations> stationsList;
    private  Fragment fragment;

    public StationsAdapter(ArrayList<Stations> stationsList, Fragment currentFragment) {
        this.fragment = currentFragment;
        this.stationsList = stationsList;
        Log.d("MusicGo","ststion list size "+stationsList.size());
    }

    @Override
    public StationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_stations,parent,false);
        return new StationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(StationViewHolder holder, final int position) {
        final Stations stations = stationsList.get(position);
        holder.updateUI(stations);
        Log.d("MusicGo"," music station " + stations.getSongName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int p = position;

                Fragment SpotifyPlayerFragment = new SpotifyPlayerFragment();
                Bundle b = new Bundle();
                b.putString("songUri",stations.getSongUri());
                b.putString("songName",stations.getSongName());
                b.putString("artistName",stations.getSongArtist());
                b.putString("songImage",stations.getImageUri());
                SpotifyPlayerFragment.setArguments(b);

                FragmentManager fragmentManager = fragment.getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.main_container,SpotifyPlayerFragment).addToBackStack(null).commit();

            }
        });
    }

    @Override
    public int getItemCount() {
        return stationsList.size();
    }
}
