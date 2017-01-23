package com.example.vinam.musicgo.application;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;

/**
 * Created by vinamravinay12 on 12/12/2016.
 */

public class MusicGoApplication extends Application {

    private static MusicGoApplication musicGoApplication = null;
    private static Context mContext;
    private Bitmap songImage;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        musicGoApplication = this;
    }
    public static MusicGoApplication getInstance(){
        return musicGoApplication;
    }
    public static Context getAppContext(){
        return mContext;
    }

    public Bitmap getSongImage() {
        return songImage;
    }

    public void setSongImage(Bitmap songImage) {
        this.songImage = songImage;
    }

}
