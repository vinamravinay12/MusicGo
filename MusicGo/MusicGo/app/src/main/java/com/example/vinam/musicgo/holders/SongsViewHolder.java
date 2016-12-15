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
        songImage = (ImageView)itemView.findViewById(R.id.song_image);
        songDetails = (TextView)itemView.findViewById(R.id.song_name);
        artistName = (TextView)itemView.findViewById(R.id.song_artist);
    }

    public void updateUI(Songs song){
        String uri = song.getSongImageUrlSmall();

        try {
            SongsViewHolder.DecodeBitMap decodeBitMap = new SongsViewHolder.DecodeBitMap(songImage,song);
            decodeBitMap.execute();

           songDetails.setText(song.getSongName()+" - " +song.getAlbumName());
            Log.d("watchdogs","song image " + uri);
            artistName.setText(song.getArtistName());
        } catch(Exception e){
            Log.d("MusicGo","exception just generated "+e.getMessage());
        }
    }
    public Bitmap decodeUri(String imageUri){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        InputStream in = null;

        try {
            in = new java.net.URL(imageUri).openStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Bitmap output = null;
        output =  BitmapFactory.decodeStream(in,new Rect(-1,-1,-1,-1),options);
        Log.d("ImageBitmap","original width and height " + options.outWidth+" :: "+options.outHeight);
        Boolean scaleByHeight = Math.abs(options.outHeight - 100) >= Math.abs(options.outWidth - 100);
        if(options.outHeight * options.outWidth * 2 >= 16384){
            double sampleSize = scaleByHeight ? options.outHeight/4000 : options.outWidth/2000;
            options.inSampleSize = (int) Math.pow(2d,Math.floor(Math.log(sampleSize)/Math.log(2d)));
        }
        options.inJustDecodeBounds = false;
        options.inTempStorage = new byte[16*1024];


        output =  BitmapFactory.decodeStream(in,new Rect(-1,-1,-1,-1),options);
        //Bitmap output = BitmapFactory.decodeStream(in);
        Bitmap scaled = Bitmap.createScaledBitmap(output,170,170,false);

        return scaled;

    }

    class DecodeBitMap extends AsyncTask<Void,Void,Bitmap> {
        private final WeakReference<ImageView> mImageViewWeakReerence;
        private Songs song;

        public DecodeBitMap(ImageView imageView,Songs instaImage) {
            this.mImageViewWeakReerence = new WeakReference<ImageView>(imageView);
            this.song = instaImage;
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
        }
    }
}


