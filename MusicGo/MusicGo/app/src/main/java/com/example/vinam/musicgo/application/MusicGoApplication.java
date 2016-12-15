package com.example.vinam.musicgo.application;

import android.app.Application;
import android.content.Context;

/**
 * Created by vinamravinay12 on 12/12/2016.
 */

public class MusicGoApplication extends Application {

    private static MusicGoApplication musicGoApplication = new MusicGoApplication();
    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
    }
    public static Context getAppContext(){
        return mContext;
    }
}
