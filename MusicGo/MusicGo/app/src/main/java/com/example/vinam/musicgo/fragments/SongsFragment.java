package com.example.vinam.musicgo.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.vinam.musicgo.R;
import com.example.vinam.musicgo.Services.DataService;
import com.example.vinam.musicgo.activities.PlaylistsActivity;
import com.example.vinam.musicgo.adapters.PlaylistsAdapter;
import com.example.vinam.musicgo.adapters.SongsAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 *
 * to handle interaction events.
 * Use the {@link SongsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SongsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";


    // TODO: Rename and change types of parameters
    private static int type;
    private String key="";

   // private OnFragmentInteractionListener mListener;

    public SongsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     *
     *
     *
     * @return A new instance of fragment SongsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SongsFragment newInstance() {
        SongsFragment fragment = new SongsFragment();
        //type = param1;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            key  = getArguments().getString("playlist_id");
            type = getArguments().getInt("type");
            Log.d("TAG","key is  featured " + key);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_songs, container, false);
        RecyclerView recyclerView = (RecyclerView)view.findViewById(R.id.recyler_songs);
        recyclerView.setHasFixedSize(true);
        SongsAdapter songsAdapter;
        Log.d("TAG","playlist_type is " + type);
        if(type == PlaylistsActivity.STATION_TYPE_MY_SONG){
           songsAdapter = new SongsAdapter(DataService.getInstance().getUserSongsList(),this,"my_song");

            // recyclerView.setBackgroundResource(R.drawable.side_nav_bar_featured);
        } else if(type == PlaylistsActivity.STATION_TYPE_USER_TRACKS){
            StringBuffer userTrackType = new StringBuffer();
            userTrackType.append("user_playlists").append(",").append(key);
            //Log.d("TAG","user playlists size "+ DataService.getInstance().getUserPlaylistsTracksMap().size());
            songsAdapter = new SongsAdapter(DataService.getInstance().getUserPlaylistsTracksMap().get(key),this,userTrackType.toString());
        }
        else{
            StringBuffer featuredType = new StringBuffer();
            featuredType.append("featured_playlists").append(",").append(key);
          //  Log.d("TAG","featured playlist tracks sizde " + DataService.getInstance().getFeaturedPlaylistsTracksMap().get(key).size());
            songsAdapter = new SongsAdapter(DataService.getInstance().getFeaturedPlaylistsTracksMap().get(key),this,featuredType.toString());
        }
        recyclerView.setAdapter(songsAdapter);
        recyclerView.addItemDecoration(new HorizontalSpaceItemDecorator(30));


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getActivity());
        linearLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
       /* if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }*/
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        /*if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
    }

    @Override
    public void onDetach() {
        super.onDetach();
     //   mListener = null;
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
