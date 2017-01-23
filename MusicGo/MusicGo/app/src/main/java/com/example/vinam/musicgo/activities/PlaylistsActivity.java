package com.example.vinam.musicgo.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.vinam.musicgo.R;
import com.example.vinam.musicgo.fragments.HomeFragment;
import com.example.vinam.musicgo.fragments.MainFragment;
import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationResponse;
import com.spotify.sdk.android.player.Player;

import org.json.JSONException;
import org.json.JSONObject;

public class PlaylistsActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        MainFragment.OnMainFragmentInteractionListener{

    public static final String CLIENT_ID = "5e41ef158c894daca97c62ca2e033d9a";
    // TODO: Replace with your redirect URI
    //private static final String REDIRECT_URI = "http:/localhost:1337/callback";
    private Player player;
    public static  boolean userLoggedIn = false;
    public static String AUTH_TOKEN;
    public static final String BASE_URL = "http://192.168.0.13:3100";
    public static final int STATION_TYPE_USER_PLAYLIST = 0;
    public static final int STATION_TYPE_MY_SONG = 1;
    public static final int STATION_TYPE_FEATURED = 2;
    public static final int STATION_TYPE_USER_TRACKS =3;
    public static final int STATION_TYPE_FEATURED_TRACKS = 4;
    public static PlaylistsActivity getInstance() {
        return ourInstance;
    }

    private static PlaylistsActivity ourInstance = new PlaylistsActivity();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlists);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        /*fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
*/
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        FragmentManager manager = getSupportFragmentManager();

                HomeFragment homeFragment = HomeFragment.newInstance();
                manager.beginTransaction().replace(R.id.main_container, homeFragment).addToBackStack(null).commit();



    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1337) {
            AuthenticationResponse response = AuthenticationClient.getResponse(resultCode, data);
            Log.d("MusicGo","token received " + response.getAccessToken());
            if (response.getType() == AuthenticationResponse.Type.TOKEN) {
                PlaylistsActivity.AUTH_TOKEN  = response.getAccessToken();
                PlaylistsActivity.userLoggedIn = true;
                /// Config playerConfig = new Config(this, response.getAccessToken(), CLIENT_ID);
                sendAuthTokenToServer();


            }
        }

    }
    public void sendAuthTokenToServer(){
        final String url = BASE_URL+"/auth";
        JSONObject authObject = new JSONObject();
        try {
            authObject.put("token",PlaylistsActivity.AUTH_TOKEN);
            Log.d("MusicGo","token sending "+authObject.getString("token"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, authObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    if (response == null || response.equals("{}")) {
                        Log.v("MusicGo", "response null or empty");
                    } else {
                        Log.d("MusicGo", " json got " + response);
                        // PlaylistsActivity.AUTH_TOKEN = response.getString("auth_code");

                        String message = response.getString("message");
                        if(message.equalsIgnoreCase("Received Token")) {
                            FragmentManager fragmentManager = getSupportFragmentManager();
                            fragmentManager.beginTransaction().replace(R.id.main_container, new MainFragment()).addToBackStack(null).commit();
                        }


                    }
                } catch (JSONException e) {
                    Log.v("MusicGo", "Json exception " + e.getLocalizedMessage());
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.v("MusicGo", "Err : " + error.getLocalizedMessage());
            }
        });
        Volley.newRequestQueue(this).add(jsonObjectRequest);

    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.playlists, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onMainFragmentInteraction(Uri uri) {

    }




}
