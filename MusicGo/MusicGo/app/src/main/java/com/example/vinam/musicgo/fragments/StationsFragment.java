package com.example.vinam.musicgo.fragments;

import android.content.Context;
import android.graphics.Rect;
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
import com.example.vinam.musicgo.model.Playlists;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 *
 * to handle interaction events.
 * Use the {@link StationsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StationsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_STATION_TYPE = "Station_Type";

    private int stationType;

    // TODO: Rename and change types of parameters


    public StationsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param stationType Parameter 1.
     *
     * @return A new instance of fragment StationsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StationsFragment newInstance(int stationType) {
        StationsFragment fragment = new StationsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_STATION_TYPE, stationType);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            stationType = getArguments().getInt(ARG_STATION_TYPE);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_stations, container, false);
        RecyclerView recyclerView = (RecyclerView)view.findViewById(R.id.recyler_stations);
        recyclerView.setHasFixedSize(true);

        PlaylistsAdapter playlistsAdapter;
        SongsAdapter songsAdapter;
        if(stationType == PlaylistsActivity.STATION_TYPE_USER_PLAYLIST){
            playlistsAdapter = new PlaylistsAdapter(DataService.getInstance().getUserPlaylists(),this,PlaylistsActivity.STATION_TYPE_USER_PLAYLIST);
            recyclerView.setAdapter(playlistsAdapter);
           // recyclerView.setBackgroundResource(R.drawable.side_nav_bar_featured);
        }else{
            playlistsAdapter = new PlaylistsAdapter(DataService.getInstance().getFeaturedPlaylists(),this,PlaylistsActivity.STATION_TYPE_FEATURED);
          //  recyclerView.setBackgroundResource(R.drawable.side_nav_bar_mood);
            recyclerView.setAdapter(playlistsAdapter);
        }

        recyclerView.addItemDecoration(new HorizontalSpaceItemDecorator(30));


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),2);
        gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(gridLayoutManager);
        return  view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
      /*  if (mListener != null) {
            mListener.onStationsFragmentInteraction(uri);
        }*/
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
       /* if (context instanceof OnStationsFragmentInteractionListener) {
            mListener = (OnStationsFragmentInteractionListener) context;
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
    /*public interface OnStationsFragmentInteractionListener {
        // TODO: Update argument type and name
        void onStationsFragmentInteraction(Uri uri);
    }*/
}

class HorizontalSpaceItemDecorator extends RecyclerView.ItemDecoration {
    private final int spacer;
    public HorizontalSpaceItemDecorator(int spacer){
        this.spacer = spacer;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.right = spacer;
    }
}
