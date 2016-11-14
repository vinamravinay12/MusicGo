package com.example.vinam.musicgo.model;

/**
 * Created by vinam on 11/6/2016.
 */

public class Stations {
    private String songName;
    private String imageUri;

    public String getSongUri() {
        return songUri;
    }

    private String songUri;

    public String getSongName() {
        return songName;
    }

    public String getImageUri() {
        return imageUri;
    }

    public String getSongArtist() {
        return songArtist;
    }

    private String songArtist;

    public Stations(String songName, String songArtist, String imageUri,String songUri) {
        this.songName = songName;
        this.imageUri = imageUri;
        this.songArtist = songArtist;
        this.songUri = songUri;
    }

}
