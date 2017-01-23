package com.example.vinam.musicgo.model;

import android.graphics.Bitmap;

/**
 * Created by vinam on 11/6/2016.
 */

public class Playlists {
    String playlistId;
    String playlistImageUrl;
    String playlistName;
    String ownerId;
    Bitmap playlistImage;

    public String getPlaylistId() {
        return playlistId;
    }

    public String getPlaylistImageUrl() {
        return playlistImageUrl;
    }

    public String getOwnerId() { return ownerId; }
    public String getPlaylistName() {
        return playlistName;
    }
    public Bitmap getPlaylistImage() {
        return playlistImage;
    }

    public void setPlaylistImage(Bitmap playlistImage) {
        this.playlistImage = playlistImage;
    }
    public Playlists(String playlistId, String playlistImageUrl,String playlistName,String ownerId) {
        this.playlistId = playlistId;
        this.playlistImageUrl = playlistImageUrl;
        this.playlistName = playlistName;
        this.ownerId = ownerId;
    }
}