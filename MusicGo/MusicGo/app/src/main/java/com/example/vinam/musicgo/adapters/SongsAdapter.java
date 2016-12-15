package com.example.vinam.musicgo.adapters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.vinam.musicgo.R;
import com.example.vinam.musicgo.fragments.SongsFragment;
import com.example.vinam.musicgo.fragments.SpotifyPlayerFragment;
import com.example.vinam.musicgo.holders.PlaylistViewHolder;
import com.example.vinam.musicgo.holders.SongsViewHolder;
import com.example.vinam.musicgo.model.Songs;

import java.util.ArrayList;

import static com.example.vinam.musicgo.R.menu.playlists;

/**
 * Created by vinamravinay12 on 12/11/2016.
 */

public class SongsAdapter extends RecyclerView.Adapter<SongsViewHolder> {

    private ArrayList<Songs> songsList = new ArrayList<>();
    private Fragment fragment;

    public SongsAdapter(ArrayList<Songs> songsList,Fragment fragment){
        this.songsList = songsList;
        this.fragment = fragment;
    }
    @Override
    public SongsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_song,parent,false);
        return new SongsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SongsViewHolder holder, final int position) {
        final Songs song = songsList.get(position);
        holder.updateUI(song);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int p = position;

                SpotifyPlayerFragment spotifyPlayerFragment = new SpotifyPlayerFragment();
                Bundle b = new Bundle();
                b.putString("song_name",song.getSongName());
                b.putString("album_name",song.getAlbumName());
                b.putString("artist_name",song.getArtistName());
                b.putString("song_duration",song.getSongDuration());
                b.putString("song_uri",song.getSongUri());
                b.putString("song_image_url",song.getSongImageUrl());
                spotifyPlayerFragment.setArguments(b);

                FragmentManager fragmentManager = fragment.getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.main_container,spotifyPlayerFragment).addToBackStack(null).commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return 0;
    }
}