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
import com.example.vinam.musicgo.fragments.SongsFragment;
import com.example.vinam.musicgo.fragments.SpotifyPlayerFragment;
import com.example.vinam.musicgo.holders.PlaylistViewHolder;
import com.example.vinam.musicgo.model.Playlists;

import java.util.ArrayList;

/**
 * Created by vinam on 11/6/2016.
 */

public class PlaylistsAdapter extends RecyclerView.Adapter<PlaylistViewHolder> {

    private ArrayList<Playlists> playlistsList;
    private  Fragment fragment;
    private int type;

    public PlaylistsAdapter(ArrayList<Playlists> playlistsList, Fragment currentFragment,int type) {
        this.fragment = currentFragment;
        this.playlistsList = playlistsList;
        this.type = type;
        Log.d("MusicGo","ststion list size "+ playlistsList.size());
    }

    @Override
    public PlaylistViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_playlists,parent,false);
        return new PlaylistViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PlaylistViewHolder holder, final int position) {
        final Playlists playlists = playlistsList.get(position);
        holder.updateUI(playlists);
        Log.d("MusicGo"," music station " + playlists.getPlaylistName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int p = position;

                SongsFragment songsFragment = SongsFragment.newInstance();
                Bundle b = new Bundle();
                b.putString("playlist_id", playlists.getPlaylistId());
                if(type == PlaylistsActivity.STATION_TYPE_USER_PLAYLIST)
                    b.putInt("type",PlaylistsActivity.STATION_TYPE_USER_TRACKS);
                else{
                    b.putInt("type",PlaylistsActivity.STATION_TYPE_FEATURED_TRACKS);
                }
                songsFragment.setArguments(b);
                FragmentManager fragmentManager = fragment.getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.main_container,songsFragment).addToBackStack(null).commit();

            }
        });
    }

    @Override
    public int getItemCount() {
        return playlistsList.size();
    }
}
