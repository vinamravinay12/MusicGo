package com.example.vinam.musicgo.model;

/**
 * Created by vinam on 11/6/2016.
 */

public class Playlists {
    String playlistId;
    String playlistImageUrl;
    String playlistName;

    public String getPlaylistId() {
        return playlistId;
    }

    public String getPlaylistImageUrl() {
        return playlistImageUrl;
    }


    public String getPlaylistName() {
        return playlistName;
    }

    public Playlists(String playlistId, String playlistImageUrl,String playlistName) {
        this.playlistId = playlistId;
        this.playlistImageUrl = playlistImageUrl;
        this.playlistName = playlistName;
    }
}