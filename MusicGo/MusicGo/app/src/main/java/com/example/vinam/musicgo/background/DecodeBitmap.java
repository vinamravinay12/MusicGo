package com.example.vinam.musicgo.background;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import com.example.vinam.musicgo.application.MusicGoApplication;
import com.example.vinam.musicgo.model.Playlists;
import com.example.vinam.musicgo.model.Songs;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;

/**
 * Created by vinamravinay12 on 12/19/2016.
 */

public class DecodeBitmap extends AsyncTask<Void,Void,Bitmap> {
    private  WeakReference<ImageView> mImageViewWeakReerence = null;
    private Songs song;

    public DecodeBitmap(ImageView imageView, Songs song) {
        this.mImageViewWeakReerence = new WeakReference<ImageView>(imageView);
        this.song = song;
    }


    @Override
    protected Bitmap doInBackground(Void... voids) {
        return decodeUri(song.getSongImageUrlSmall());
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        Log.d("ImageBitmap","modified width and height " + bitmap.getWidth()+" :: "+bitmap.getHeight());
        final ImageView imgView = mImageViewWeakReerence.get();
        if(imgView != null){
            imgView.setImageBitmap(bitmap);
        }
        song.setSongImage(bitmap);
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
        Bitmap scaled = null;
        scaled = Bitmap.createScaledBitmap(output, 70, 70, true);
        return scaled;
    }
}

