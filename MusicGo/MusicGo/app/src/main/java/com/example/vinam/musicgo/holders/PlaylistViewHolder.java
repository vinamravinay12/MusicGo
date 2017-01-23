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
import com.example.vinam.musicgo.model.Playlists;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;

/**
 * Created by vinam on 11/6/2016.
 */

public class PlaylistViewHolder extends RecyclerView.ViewHolder {

    private TextView playlistName;
    private ImageView playlistImageUri;
        private  TextView playlistOwner;
    public PlaylistViewHolder(View itemView) {
        super(itemView);
        this.playlistImageUri= (ImageView)itemView.findViewById(R.id.playlist_image);
        this.playlistName = (TextView)itemView.findViewById(R.id.playlist_name);
        this.playlistOwner = (TextView)itemView.findViewById(R.id.playlist_owner);
    }

    public void updateUI(Playlists playlists){
        playlistName.setText(playlists.getPlaylistName());

        try {
            DecodeBitMap decodeBitMap = new DecodeBitMap(playlistImageUri,playlists);
            decodeBitMap.execute();

        } catch(Exception e){
            Log.d("MusicGo","exception just generated "+e.getMessage());
        }
    }
    public Bitmap decodeUri(String imageUri){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        Boolean scaleByHeight = Math.abs(options.outHeight - 100) >= Math.abs(options.outWidth - 100);
        if (options.outHeight * options.outWidth * 2 >= 16384) {
            double sampleSize = scaleByHeight ? options.outHeight / 1000 : options.outWidth / 1000;
            options.inSampleSize = (int) Math.pow(2d, Math.floor(Math.log(sampleSize) / Math.log(2d)));
        }
        options.inJustDecodeBounds = false;
        options.inTempStorage = new byte[16 * 1024];
        InputStream in = null;
        try {
            in = new java.net.URL(imageUri).openStream();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Bitmap output = BitmapFactory.decodeStream(in, new Rect(-1, -1, -1, -1), options);
        Bitmap scaled = Bitmap.createScaledBitmap(output, 170, 170, true);
        return scaled;
    }

class DecodeBitMap extends AsyncTask<Void,Void,Bitmap> {
    private final WeakReference<ImageView> mImageViewWeakReerence;
    private Playlists playlists;

    public DecodeBitMap(ImageView imageView, Playlists instaImage) {
        this.mImageViewWeakReerence = new WeakReference<ImageView>(imageView);
        this.playlists = instaImage;
    }

    @Override
    protected Bitmap doInBackground(Void... voids) {

        return decodeUri(playlists.getPlaylistImageUrl());
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
       // Log.d("ImageBitmap","modified width and height " + bitmap.getWidth()+" :: "+bitmap.getHeight());
        final ImageView imgView = mImageViewWeakReerence.get();
        if(imgView != null){
            imgView.setImageBitmap(bitmap);
        }
    }
}
}
