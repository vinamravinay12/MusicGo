package com.example.vinam.musicgo.model;

/**
 * Created by vinamravinay12 on 12/11/2016.
 */

public class Songs {
    String songId;
    String songName;
    String albumName;
    String artistName;
    String songImageUrl;
    String songImageUrlSmall;
    String songDuration;
    String songUri;

    public String getSongId() {
        return songId;
    }

    public String getSongName() {
        return songName;
    }

    public String getAlbumName() {
        return albumName;
    }

    public String getArtistName() {
        return artistName;
    }

    public String getSongImageUrl() {
        return songImageUrl;
    }

    public String getSongImageUrlSmall() {
        return songImageUrlSmall;
    }

    public String getSongDuration() {
        return songDuration;
    }

    public String getSongUri() {
        return songUri;
    }

    public Songs(String songId, String songName, String albumName, String artistName, String songImageUrl, String songImageUrlSmall, String songDuration, String songUri) {

        this.songId = songId;
        this.songName = songName;
        this.albumName = albumName;
        this.artistName = artistName;
        this.songImageUrl = songImageUrl;
        this.songImageUrlSmall = songImageUrlSmall;
        this.songDuration = songDuration;
        this.songUri = songUri;
    }
}
