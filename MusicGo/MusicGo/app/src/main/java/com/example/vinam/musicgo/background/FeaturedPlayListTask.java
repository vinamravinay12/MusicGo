package com.example.vinam.musicgo.background;

import android.os.AsyncTask;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.vinam.musicgo.Services.DataService;
import com.example.vinam.musicgo.activities.PlaylistsActivity;
import com.example.vinam.musicgo.application.MusicGoApplication;
import com.example.vinam.musicgo.model.Playlists;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by vinamravinay12 on 12/12/2016.
 */

public class FeaturedPlayListTask extends AsyncTask<String,String,String> {
    int myDownloadCount = 0;
    boolean result;
    private String playlistId ;
    private String playlistName;
    private String playlistImageUrl;
    private String playlistOwner;
    String message;
    static boolean mySongsOutput;
    @Override
    protected String doInBackground(String... strings) {


        final String url = strings[0];


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if (myDownloadCount == 0) {
                    try {
                        if (response == null || response.equals("{}")) {
                            Log.v("MusicGo", "response null or empty");
                            result = false;
                        } else {
                            Log.d("MusicGo", " feautered json got " + response);
                            PlaylistsActivity.AUTH_TOKEN = response.getString("auth_code");
                            if (PlaylistsActivity.AUTH_TOKEN != null) {
                                PlaylistsActivity.userLoggedIn = true;
                            }
                            JSONArray resp = response.getJSONArray("result");
                            for (int i = 0; i < resp.length(); i++) {

                                JSONObject trackObject = resp.getJSONObject(i);

                                playlistId = trackObject.getString("playlist_id");
                                playlistName = trackObject.getString("playlist_name");
                                playlistImageUrl = trackObject.getString("playlist_image");
                                playlistOwner = trackObject.getString("playlist_owner");
                                DataService.getInstance().setFeaturedPlaylists(playlistId,playlistImageUrl,playlistName,playlistOwner);
                            }
                            message = "download successful";
                        }
                    } catch (JSONException e) {
                        Log.v("MusicGo", "Json exception " + e.getLocalizedMessage());
                    }

                }
                myDownloadCount++;
                if(message!= null && message.equalsIgnoreCase("download successful")){
                    ArrayList<Playlists> downloadedPlaylists = DataService.getInstance().getFeaturedPlaylists();
                    for(Playlists playlist : downloadedPlaylists){
                        new FeaturedPlaylistTracksDownloadTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,new String[]{
                                PlaylistsActivity.BASE_URL+"/query/"+PlaylistsActivity.STATION_TYPE_FEATURED_TRACKS+"/"+playlist.getPlaylistId()+"/"+playlist.getOwnerId()
                                ,playlist.getPlaylistId()});
                    }
                }
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

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

    }
}
