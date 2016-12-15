package com.example.vinam.musicgo.Services;

import android.util.Log;

import com.example.vinam.musicgo.model.Playlists;
import com.example.vinam.musicgo.model.Songs;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by vinam on 11/6/2016.
 */

public class DataService  {

    private static DataService ourInstance = new DataService();

   private  ArrayList<Playlists> userPlaylists = new ArrayList<>();
    private Map<String,ArrayList<Songs>> userPlaylistsTracksMap = new TreeMap<String,ArrayList<Songs>>();
    private Map<String,ArrayList<Songs>> featurePlaylistsTracksMap = new TreeMap<String,ArrayList<Songs>>();
    private  ArrayList<Songs>mySongList = new ArrayList<>();
    private  ArrayList<Playlists>featuredPlaylists = new ArrayList<>();
    private ArrayList<Songs>userPlaylistTracks = new ArrayList<>();
    private ArrayList<Songs> featurePlaylistTracks = new ArrayList<>();
    public static DataService getInstance(){
        return ourInstance;
    }

    private DataService(){

    }

    public void setUserSongsList(String songId,String songName,String albumName,String artistName,String imageUrl,String imageUrlSmall,String songDuration,String songUri){
        //Log.d("MusicGo","set the music list values " +songName +" :: "+songArtist);
        Songs song = new Songs(songId,songName,albumName,artistName,imageUrl,imageUrlSmall,songDuration,songUri);
        if(!mySongList.contains(songId))
            mySongList.add(song);
        Log.d("MusicGo","my song list " + mySongList.size());


    }
    public ArrayList<Songs> getUserSongsList(){
        Log.d("MusicGo","song list size here "+ mySongList.size());
        return mySongList;

    }

    public void setFeaturedPlaylists(String playlistId, String playlistImageUrl, String playlistName){
        featuredPlaylists.add(new Playlists(playlistId,playlistImageUrl,playlistName));
    }
    public ArrayList<Playlists> getFeaturedPlaylists(){
       return featuredPlaylists;

    }

    public void setUserPlaylists(String playlistId, String playlistImageUrl, String playlistName){
        userPlaylists.add(new Playlists(playlistId,playlistImageUrl,playlistName));
    }
    public ArrayList<Playlists> getUserPlaylists(){
      return userPlaylists;

    }

    public void setUserPlaylistsTracksMap(String key,String songId,String songName,String albumName,String artistName,String imageUrl,String imageUrlSmall,String songDuration,String songUri){
        if(!userPlaylistsTracksMap.containsKey(key)){
            userPlaylistTracks.add(new Songs(songId,songName,albumName,artistName,imageUrl,imageUrlSmall,songDuration,songUri));
            userPlaylistsTracksMap.put(key,userPlaylistTracks);
        }else{
            userPlaylistTracks = userPlaylistsTracksMap.get(key);
            userPlaylistTracks.add(new Songs(songId,songName,albumName,artistName,imageUrl,imageUrlSmall,songDuration,songUri));
            userPlaylistsTracksMap.put(key,userPlaylistTracks);
        }
    }

    public Map<String,ArrayList<Songs>> getUserPlaylistsTracksMap(){
        return userPlaylistsTracksMap;
    }
    public void setFeaturedPlaylistsTracksMap(String key,String songId,String songName,String albumName,String artistName,String imageUrl,String imageUrlSmall,String songDuration,String songUri){
        if(!featurePlaylistsTracksMap.containsKey(key)){
           featurePlaylistTracks.add(new Songs(songId,songName,albumName,artistName,imageUrl,imageUrlSmall,songDuration,songUri));
            featurePlaylistsTracksMap.put(key,userPlaylistTracks);
        }else{
            featurePlaylistTracks = userPlaylistsTracksMap.get(key);
           featurePlaylistTracks.add(new Songs(songId,songName,albumName,artistName,imageUrl,imageUrlSmall,songDuration,songUri));
            featurePlaylistsTracksMap.put(key,userPlaylistTracks);
        }
    }

    public Map<String,ArrayList<Songs>> getFeaturedPlaylistsTracksMap(){
        return featurePlaylistsTracksMap;
    }
}
