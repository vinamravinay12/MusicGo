package com.example.vinam.musicgo.holders;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.vinam.musicgo.R;
import com.example.vinam.musicgo.background.DecodeBitmap;
import com.example.vinam.musicgo.model.Playlists;
import com.example.vinam.musicgo.model.Songs;

import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;

/**
 * Created by vinamravinay12 on 12/11/2016.
 */

public class SongsViewHolder extends RecyclerView.ViewHolder {
    private ImageView songImage;
    private TextView songDetails;
    private TextView artistName;
    public SongsViewHolder(View itemView) {
        super(itemView);
      //  Log.d("TAG","inside songs view holder constructor");
        songImage = (ImageView)itemView.findViewById(R.id.song_image);
        songDetails = (TextView)itemView.findViewById(R.id.song_name);
        artistName = (TextView)itemView.findViewById(R.id.song_artist);
    }

    public void updateUI(Songs song){
        songDetails.setText(song.getSongName()+" - " +song.getAlbumName());
        // Log.d("watchdogs","song image " + uri);
        artistName.setText(song.getArtistName());
        //String uri = song.getSongImageUrlSmall();
        if(song.getSongImage() == null) {
            try {
                DecodeBitmap decodeBitMap = new DecodeBitmap(songImage, song);
                decodeBitMap.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

            } catch (Exception e) {
                Log.d("MusicGo", "exception just generated " + e.getMessage());
            }
        } else{
            songImage.setImageBitmap(song.getSongImage());
        }
        try {



        } catch(Exception e){
            Log.d("MusicGo","exception just generated "+e.getMessage());
        }
    }



}


