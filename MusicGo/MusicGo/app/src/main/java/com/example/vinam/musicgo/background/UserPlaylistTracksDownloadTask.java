package com.example.vinam.musicgo.background;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.vinam.musicgo.Services.DataService;
import com.example.vinam.musicgo.activities.PlaylistsActivity;
import com.example.vinam.musicgo.application.MusicGoApplication;
import com.example.vinam.musicgo.holders.SongsViewHolder;
import com.example.vinam.musicgo.model.Songs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.concurrent.ExecutionException;

/**
 * Created by vinamravinay12 on 12/12/2016.
 */

public class UserPlaylistTracksDownloadTask extends AsyncTask<String,String,String> {
    int myDownloadCount = 0;
    String songId;
    String songName;
    String albumName;
    String artistName;
    String songImageUrl;
    String songImageUrlSmall;
    String songDuration;
    String songUri;
    String message = "";

    @Override
    protected String doInBackground(String... strings) {

        final String url = strings[0];
        final String key = strings[1];

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("playlist_id", key);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    if (response == null || response.equals("{}")) {
                        Log.v("MusicGo", "response null or empty");
                    } else {

                        Log.d("MusicGo", " user track json got " + response);
                        PlaylistsActivity.AUTH_TOKEN = response.getString("auth_code");
                        if (PlaylistsActivity.AUTH_TOKEN != null) {
                            PlaylistsActivity.userLoggedIn = true;
                        }
                        JSONArray resp = response.getJSONArray("result");
                        for (int i = 0; i < resp.length(); i++) {

                            JSONObject trackObject = resp.getJSONObject(i);

                            songId = trackObject.getString("song_id");
                            songName = trackObject.getString("song_name");
                            albumName = trackObject.getString("album_name");
                            artistName = trackObject.getString("artist_name");
                            songImageUrl = trackObject.getString("image_url");
                            songImageUrlSmall = trackObject.getString("image_url_small");
                            songDuration = trackObject.getString("duration");
                            songUri = trackObject.getString("song_uri");
                            DataService.getInstance().setUserPlaylistsTracksMap(key, songId, songName, albumName,
                                    artistName, songImageUrl, songImageUrlSmall, songDuration, songUri);
                        }

                    }
                } catch (JSONException e) {
                    Log.v("MusicGo", "Json exception " + e.getLocalizedMessage());
                }
                message = "download successful";


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.v("MusicGo", "Err : " + error.getLocalizedMessage());
            }
        });
        Volley.newRequestQueue(MusicGoApplication.getAppContext()).add(jsonObjectRequest);

        return message;
    }
}
