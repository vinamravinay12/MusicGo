package com.example.vinam.musicgo.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.vinam.musicgo.R;
import com.example.vinam.musicgo.Services.DataService;
import com.example.vinam.musicgo.activities.PlaylistsActivity;
import com.example.vinam.musicgo.model.Stations;
import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;
import com.spotify.sdk.android.player.Config;
import com.spotify.sdk.android.player.ConnectionStateCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MainFragment.OnMainFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainFragment extends Fragment implements View.OnClickListener,ConnectionStateCallback {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    String songName = "";
    String imageUrl = "";
    String artistName = "";
    String songUri = "";
    static boolean mySongsOutput;


    static boolean featuredPlaylistOutput;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    LinearLayout featuredSongsLayout;
    LinearLayout mySongsLayout;
    LinearLayout moodSongsLayout;
    private boolean result = false;
    public static int myDownloadCount = 0;
    private OnMainFragmentInteractionListener mListener;

    public MainFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MainFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MainFragment newInstance(String param1, String param2) {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        featuredSongsLayout = (LinearLayout) view.findViewById(R.id.featured_songs_layout);
        mySongsLayout = (LinearLayout) view.findViewById(R.id.my_songs_layout);
        moodSongsLayout = (LinearLayout) view.findViewById(R.id.mood_songs_layout);
        featuredSongsLayout.setOnClickListener(this);
        mySongsLayout.setOnClickListener(this);
        featuredSongsLayout.setOnClickListener(this);
        downloadMySongsData();
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("MusicGo","Inside onActivity resylt");
        super.onActivityResult(requestCode, resultCode, data);

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onMainFragmentInteraction(uri);
        }
    }

    public void downloadMySongsData() {

        final String url = "http://192.168.0.18:3100/query/" + StationsFragment.STATION_TYPE_MY_SONG;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if (myDownloadCount == 0) {
                    try {
                        if (response == null || response.equals("{}")) {
                            Log.v("MusicGo", "response null or empty");
                            result = false;
                        } else {

                            Log.d("MusicGo", " json got " + response);
                            PlaylistsActivity.AUTH_TOKEN = response.getString("auth_code");
                            if (PlaylistsActivity.AUTH_TOKEN != null) {
                                PlaylistsActivity.userLoggedIn = true;
                            }
                            JSONArray resp = response.getJSONArray("result");
                            for (int i = 0; i < resp.length(); i++) {

                                JSONObject trackObject = resp.getJSONObject(i);

                                songName = trackObject.getString("name");
                                songUri = trackObject.getString("songurl");

                                imageUrl = trackObject.getString("imageurl");
                                artistName = trackObject.getString("artist");
                                Log.d("MusicGo", " name : " + songName + " :: " + songUri + " :: " + imageUrl + " :: " + artistName);
                                DataService.getInstance().setMyMusicStations(songName, artistName, imageUrl, songUri);
                            }
                            mySongsOutput = true;
                        }
                    } catch (JSONException e) {
                        Log.v("MusicGo", "Json exception " + e.getLocalizedMessage());
                    }
                }
                myDownloadCount++;
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.v("MusicGo", "Err : " + error.getLocalizedMessage());
            }
        });
        Volley.newRequestQueue(getContext()).add(jsonObjectRequest);

    }


    public boolean downloadFeauredPlaylist(){
        final String url="http://192.168.1.116:3100/query/"+StationsFragment.STATION_TYPE_FEATURED;
            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    JsonObjectRequest jsonObjectRequest =  new JsonObjectRequest(Request.Method.GET,url,null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {

                                if(response == null || response.equals("{}")) {
                                    result = false;
                                    Log.v("MusicGo", "response null or empty");
                                }
                                else {
                                    JSONArray resp = response.getJSONArray("result");
                                    for (int i = 0; i < resp.length(); i++) {
                                        JSONObject itemsObject = resp.getJSONObject(i);
                                        JSONObject playlist = itemsObject.getJSONObject("playlists");
                                        JSONArray itemsArray = playlist.getJSONArray("items");
                                        for (int j = 0; j < itemsArray.length(); j++) {
                                            JSONObject trackObject = itemsArray.getJSONObject(j);
                                            //JSONObject track = trackObject.getJSONObject("track");
                                            songName = trackObject.getString("name");
                                            JSONObject owner = trackObject.getJSONObject("owner");
                                            songUri = owner.getString("uri");
                                            artistName = owner.getString("type");
                                            JSONArray imageArray = trackObject.getJSONArray("images");
                                            JSONObject image = imageArray.getJSONObject(0);
                                            imageUrl = image.getString("url");
                                            DataService.getInstance().setFeaturedStations(songName, artistName, imageUrl, songUri);

                                        }
                                    }
                                    featuredPlaylistOutput= true;
                                }
                            } catch (JSONException e) {
                                Log.v("Weather","Json exception " + e.getLocalizedMessage());
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.v("Weather","Err : "+ error.getLocalizedMessage());
                        }
                    });
                    Volley.newRequestQueue(getContext()).add(jsonObjectRequest);

                }
            });


        return result;
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnMainFragmentInteractionListener) {
            mListener = (OnMainFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View view) {
        Log.d("MusicGo","get id " + view.getId());
        switch (view.getId()){
            case R.id.featured_songs_layout:{
                featuredPlaylistOutput = downloadFeauredPlaylist();
                if(featuredPlaylistOutput){
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    StationsFragment stationsFragment1 = StationsFragment.newInstance(StationsFragment.STATION_TYPE_FEATURED);
                    fragmentManager.beginTransaction().replace(R.id.main_container, stationsFragment1).addToBackStack(null).commit();
                }
                break;
            }
            case R.id.my_songs_layout:
                downloadMySongsData();
                Log.d("MusicGo","download data "+mySongsOutput);

                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    StationsFragment stationsFragment1 = StationsFragment.newInstance(StationsFragment.STATION_TYPE_MY_SONG);
                    fragmentManager.beginTransaction().replace(R.id.main_container, stationsFragment1).addToBackStack(null).commit();

                break;
            case R.id.mood_songs_layout:
                break;
        }
    }

    @Override
    public void onLoggedIn() {

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
    public interface OnMainFragmentInteractionListener {
        // TODO: Update argument type and name
        void onMainFragmentInteraction(Uri uri);
    }
}
