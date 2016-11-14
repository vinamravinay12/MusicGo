package com.example.vinam.musicgo.Services;

import android.util.Log;

import com.example.vinam.musicgo.model.Stations;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeMap;

/**
 * Created by vinam on 11/6/2016.
 */

public class DataService  {

    private static DataService ourInstance = new DataService();

   private  ArrayList<Stations> featuresList = new ArrayList<>();
    private  ArrayList<Stations>mySongList = new ArrayList<>();
    private  ArrayList<Stations>moodSongList = new ArrayList<>();
    public static DataService getInstance(){
        return ourInstance;
    }

    private DataService(){

    }

    public void setMyMusicStations(String songName,String songArtist,String imageUrl,String songUri){
        Log.d("MusicGo","set the music list values " +songName +" :: "+songArtist);
        Stations stations = new Stations(songName,songArtist,imageUrl,songUri);
        if(!mySongList.contains(songName) && !moodSongList.contains(imageUrl) && !moodSongList.contains(songArtist))
            mySongList.add(stations);
        Log.d("MusicGo","my song list " + mySongList.size());


    }
    public ArrayList<Stations> getMyMusicStations(){
        Log.d("MusicGo","song list size here "+ mySongList.size());
        return mySongList;

    }

    public void setFeaturedStations(String songName, String songArtist,String imageUrl,String songUri){
        featuresList.add(new Stations(songName,songArtist,imageUrl,songUri));
    }
    public ArrayList<Stations> getFeaturedStations(){
       return featuresList;

    }

    public void setMoodSongList(String songName,String songArtist,String imageUrl,String songUri){
        moodSongList.add(new Stations(songName,songArtist,imageUrl,songUri));
    }
    public ArrayList<Stations> getMoodStations(){
      return moodSongList;

    }
}
