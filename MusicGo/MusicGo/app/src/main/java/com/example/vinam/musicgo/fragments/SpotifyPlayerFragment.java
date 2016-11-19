package com.example.vinam.musicgo.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.vinam.musicgo.R;
import com.example.vinam.musicgo.activities.PlaylistsActivity;
import com.spotify.sdk.android.player.Config;
import com.spotify.sdk.android.player.ConnectionStateCallback;
import com.spotify.sdk.android.player.Error;
import com.spotify.sdk.android.player.Player;
import com.spotify.sdk.android.player.PlayerEvent;
import com.spotify.sdk.android.player.Spotify;
import com.spotify.sdk.android.player.SpotifyPlayer;

import java.io.IOException;
import java.io.InputStream;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 *
 * to handle interaction events.
 * Use the {@link SpotifyPlayerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SpotifyPlayerFragment extends Fragment implements View.OnClickListener,SpotifyPlayer.NotificationCallback,ConnectionStateCallback {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    String songUrl = "";
    String nameOfSong;
    String artist;
    String imageURL;
    private Player player;
    ImageView song_image_player;
    TextView songName;
    TextView artistName;
    ImageButton playButton;
    ImageButton pauseButton;
    Bitmap output;
    public SpotifyPlayerFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *

     * @return A new instance of fragment SpotifyPlayerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SpotifyPlayerFragment newInstance() {
        SpotifyPlayerFragment fragment = new SpotifyPlayerFragment();

        return fragment;


    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle b = getArguments();
        songUrl = b.getString("songUri");
        nameOfSong = b.getString("songName");
        artist = b.getString("artistName");
        imageURL = b.getString("songImage");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_spotify_player, container, false);
        song_image_player  = (ImageView) view.findViewById(R.id.song_image_player);
        songName = (TextView)view.findViewById(R.id.song_name_player);
        artistName = (TextView)view.findViewById(R.id.song_artist_player);
        playButton = (ImageButton)view.findViewById(R.id.playButton);
        pauseButton = (ImageButton)view.findViewById(R.id.pauseButton);
        playButton.setOnClickListener(this);
        pauseButton.setOnClickListener(this);

        if(PlaylistsActivity.userLoggedIn){
            Config playerConfig = new Config(getContext(),PlaylistsActivity.AUTH_TOKEN , PlaylistsActivity.CLIENT_ID);
            Spotify.getPlayer(playerConfig, this, new SpotifyPlayer.InitializationObserver() {
                @Override
                public void onInitialized(SpotifyPlayer spotifyPlayer) {
                    player = spotifyPlayer;
                    player.addConnectionStateCallback(SpotifyPlayerFragment.this);
                    player.addNotificationCallback(SpotifyPlayerFragment.this);
                }

                @Override
                public void onError(Throwable throwable) {
                    Log.e("MainActivity", "Could not initialize player: " + throwable.getMessage());
                }
            });
        }
        songName.setText(nameOfSong);
        artistName.setText(artist);
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                InputStream in = null;
                try {
                    in = new java.net.URL(imageURL).openStream();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                 output = BitmapFactory.decodeStream(in);

            }
        });

        song_image_player.setImageBitmap(output);
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
       /* if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
    }

    @Override
    public void onDetach() {
        super.onDetach();
       // mListener = null;
    }

    @Override
    public void onDestroy() {
       // super.onDestroy();
        Spotify.destroyPlayer(this);
        super.onDestroy();
    }

    @Override
    public void onPlaybackEvent(PlayerEvent playerEvent) {
        Log.d("MainActivity", "Playback event received: " + playerEvent.name());
        switch (playerEvent) {
            // Handle event type as necessary
            default:
                break;
        }
    }

    @Override
    public void onPlaybackError(Error error) {

    }

    @Override
    public void onLoggedIn() {
        player.playUri(songUrl,0,0);

    }

    @Override
    public void onLoggedOut() {

    }

    @Override
    public void onLoginFailed(int i) {

    }

    @Override
    public void onTemporaryError() {

    }

    @Override
    public void onConnectionMessage(String s) {

    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.playButton:
                player.resume();
                break;
            case R.id.pauseButton:
                player.pause();
                break;
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    /*public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }*/
}
