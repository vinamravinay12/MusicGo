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
import com.example.vinam.musicgo.model.Stations;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;

/**
 * Created by vinam on 11/6/2016.
 */

public class StationViewHolder extends RecyclerView.ViewHolder {

    private TextView songName;
    private ImageView songImageUri;
    private  TextView songArtist;
    public StationViewHolder(View itemView) {
        super(itemView);
        this.songImageUri= (ImageView)itemView.findViewById(R.id.song_image);
        this.songName = (TextView)itemView.findViewById(R.id.song_name);
        this.songArtist = (TextView)itemView.findViewById(R.id.song_artist);
    }

    public void updateUI(Stations station){
        String uri = station.getImageUri();

        try {
            DecodeBitMap decodeBitMap = new DecodeBitMap(songImageUri,station);
            decodeBitMap.execute();

            songName.setText(station.getSongName());
            Log.d("watchdogs","song image " + uri);
            songArtist.setText(station.getSongArtist());
        } catch(Exception e){
            Log.d("MusicGo","exception just generated "+e.getMessage());
        }
    }
    public Bitmap decodeUri(String imageUri){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        Boolean scaleByHeight = Math.abs(options.outHeight - 100) >= Math.abs(options.outWidth - 100);
        if(options.outHeight * options.outWidth * 2 >= 16384){
            double sampleSize = scaleByHeight ? options.outHeight/1000 : options.outWidth/1000;
            options.inSampleSize = (int) Math.pow(2d,Math.floor(Math.log(sampleSize)/Math.log(2d)));
        }
        options.inJustDecodeBounds = false;
        options.inTempStorage = new byte[512];
        InputStream in = null;
        try {
            in = new java.net.URL(imageUri).openStream();
        } catch (IOException e) {
            e.printStackTrace();
        }

       Bitmap output =  BitmapFactory.decodeStream(in,new Rect(2,2,2,2),options);
        //Bitmap output = BitmapFactory.decodeStream(in);


        return output;

    }

class DecodeBitMap extends AsyncTask<Void,Void,Bitmap> {
    private final WeakReference<ImageView> mImageViewWeakReerence;
    private Stations stations;

    public DecodeBitMap(ImageView imageView, Stations instaImage) {
        this.mImageViewWeakReerence = new WeakReference<ImageView>(imageView);
        this.stations = instaImage;
    }

    @Override
    protected Bitmap doInBackground(Void... voids) {
        return decodeUri(stations.getImageUri());
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        final ImageView imgView = mImageViewWeakReerence.get();
        if(imgView != null){
            imgView.setImageBitmap(bitmap);
        }
    }
}
}
