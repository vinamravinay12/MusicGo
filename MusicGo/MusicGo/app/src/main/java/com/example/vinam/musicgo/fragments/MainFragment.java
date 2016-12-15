package com.example.vinam.musicgo.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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
import com.example.vinam.musicgo.background.FeaturedPlayListTask;
import com.example.vinam.musicgo.background.MySongsDownloadTask;
import com.example.vinam.musicgo.background.UserPlaylistTask;
import com.spotify.sdk.android.player.ConnectionStateCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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



    static boolean featuredPlaylistOutput;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    LinearLayout userPlaylistsLayout;
    LinearLayout mySongsLayout;
    LinearLayout featuredPlaylistsLayout;
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
        new UserPlaylistTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,new String[]{PlaylistsActivity.BASE_URL+"/query/"+PlaylistsActivity.STATION_TYPE_USER_PLAYLIST});
        new FeaturedPlayListTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,new String[]{PlaylistsActivity.BASE_URL+"/query/"+PlaylistsActivity.STATION_TYPE_FEATURED});
        new MySongsDownloadTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,new String[]{PlaylistsActivity.BASE_URL+"/query/"+PlaylistsActivity.STATION_TYPE_MY_SONG});

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        userPlaylistsLayout = (LinearLayout) view.findViewById(R.id.user_playlists_layout);
        mySongsLayout = (LinearLayout) view.findViewById(R.id.my_songs_layout);
        featuredPlaylistsLayout = (LinearLayout) view.findViewById(R.id.featured_playlists_layout);
        userPlaylistsLayout.setOnClickListener(this);
        mySongsLayout.setOnClickListener(this);
        featuredPlaylistsLayout.setOnClickListener(this);

       // downloadMySongsData();
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
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        switch (view.getId()){
            case R.id.user_playlists_layout:
                    StationsFragment stationsFragmentUser = StationsFragment.newInstance(PlaylistsActivity.STATION_TYPE_USER_PLAYLIST);
                    fragmentManager.beginTransaction().replace(R.id.main_container, stationsFragmentUser).addToBackStack(null).commit();
                break;

            case R.id.my_songs_layout:
               // downloadMySongsData();
                    SongsFragment songsFragment = SongsFragment.newInstance(PlaylistsActivity.STATION_TYPE_MY_SONG);
                    fragmentManager.beginTransaction().replace(R.id.main_container, songsFragment).addToBackStack(null).commit();

                break;
            case R.id.featured_playlists_layout:
                StationsFragment stationsFragmentFeatured = StationsFragment.newInstance(PlaylistsActivity.STATION_TYPE_FEATURED);
                fragmentManager.beginTransaction().replace(R.id.main_container,stationsFragmentFeatured).addToBackStack(null).commit();
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
