package com.example.vinam.musicgo.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.vinam.musicgo.R;
import com.example.vinam.musicgo.Services.DataService;
import com.example.vinam.musicgo.activities.PlaylistsActivity;
import com.example.vinam.musicgo.model.Songs;
import com.spotify.sdk.android.player.Config;
import com.spotify.sdk.android.player.ConnectionStateCallback;
import com.spotify.sdk.android.player.Error;
import com.spotify.sdk.android.player.Player;
import com.spotify.sdk.android.player.PlayerEvent;
import com.spotify.sdk.android.player.Spotify;
import com.spotify.sdk.android.player.SpotifyPlayer;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 *
 * to handle interaction events.
 * Use the {@link SpotifyPlayerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SpotifyPlayerFragment extends Fragment implements View.OnClickListener,SpotifyPlayer.NotificationCallback,ConnectionStateCallback {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    String songUrl = "";
    String nameOfSong;
    String artist;
    String imageURL;
    String playlistType;
    private Player player;
    ImageView song_image_player;
    TextView songName;
    TextView artistName;
    ImageButton playPauseButton;
    ImageButton nextSongButton;
    ImageButton previousSongButton;
    Bitmap output;
    ImageButton rewindButton;
    ImageButton forwardButton;
    ImageButton repeatButton;
    ImageButton shuffleButton;
    TextView timeElapsed;
    TextView totalTime;
    SeekBar songProgress;
    String albumName;
    long duration;
    boolean isPlaying;
    int songPosition;
    boolean isNext;
    boolean isPrevious;


    public SpotifyPlayerFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *

     * @return A new instance of fragment SpotifyPlayerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SpotifyPlayerFragment newInstance() {
        SpotifyPlayerFragment fragment = new SpotifyPlayerFragment();

        return fragment;


    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle b = getArguments();
        songUrl = b.getString("song_uri");
        nameOfSong = b.getString("song_name");
        artist = b.getString("artist_name");
        imageURL = b.getString("song_image_url");
        albumName = b.getString("album_name");
        songPosition = b.getInt("song_position");
        Log.d("MainActivity","playback song position is " + songPosition);
        playlistType = b.getString("playlist_type");
//        duration = Long.parseLong(b.getString("song_duration"));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_spotify_player, container, false);
        song_image_player  = (ImageView) view.findViewById(R.id.song_image_player);
        songName = (TextView)view.findViewById(R.id.song_name_player);
        artistName = (TextView)view.findViewById(R.id.song_artist_player);
        playPauseButton = (ImageButton)view.findViewById(R.id.playButton);

        rewindButton = (ImageButton)view.findViewById(R.id.rewindButton);
        forwardButton = (ImageButton)view.findViewById(R.id.forwardButton);
        timeElapsed = (TextView)view.findViewById(R.id.time_elapsed);
        totalTime = (TextView)view.findViewById(R.id.total_time);
        songProgress = (SeekBar)view.findViewById(R.id.seekSong);
        previousSongButton = (ImageButton)view.findViewById(R.id.previous_song);
        nextSongButton = (ImageButton)view.findViewById(R.id.next_song);
        repeatButton= (ImageButton)view.findViewById(R.id.repeat_song);
        shuffleButton = (ImageButton)view.findViewById(R.id.shuffle_song);
        playPauseButton.setOnClickListener(this);
        rewindButton.setOnClickListener(this);
        forwardButton.setOnClickListener(this);
        nextSongButton.setOnClickListener(this);
        previousSongButton.setOnClickListener(this);
        if(PlaylistsActivity.userLoggedIn){
            Config playerConfig = new Config(getContext(),PlaylistsActivity.AUTH_TOKEN , PlaylistsActivity.CLIENT_ID);
            Spotify.getPlayer(playerConfig, this, new SpotifyPlayer.InitializationObserver() {
                @Override
                public void onInitialized(SpotifyPlayer spotifyPlayer) {
                    player = spotifyPlayer;
                    player.addConnectionStateCallback(SpotifyPlayerFragment.this);
                    player.addNotificationCallback(SpotifyPlayerFragment.this);
                }

                @Override
                public void onError(Throwable throwable) {
                    Log.e("MainActivity", "Could not initialize player: " + throwable.getMessage());
                }
            });
        }
        songName.setText(nameOfSong+" - " + albumName);
        artistName.setText(artist);
        DecodeBitmap decodeBitmap = new DecodeBitmap(song_image_player,imageURL);

        decodeBitmap.execute();
        /*long seconds = (duration/1000)%60;
        long minutes = (duration/(1000*60))%60;
        long hour = (duration/(1000*60*60))%24;
        final long[] elapsedTime = new long[1];
        String formattedTime ="";
        if(hour > 0){
           formattedTime = String.format("%02d:%02d:%02d",hour,minutes,seconds);
        } else
            formattedTime = String.format("%02d:%02d",minutes,seconds);*/
      //  duration = duration/1000;
        /*songProgress.setMax((int)duration);
        songProgress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progress = 0;
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                progress = i;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                elapsedTime[0] =  progress;
            }
        });
        totalTime.setText(formattedTime);
        String elapsedTimeFormat = "";
        while(elapsedTime[0] < duration){
            long elapsedSeconds = elapsedTime[0] %60;
            long elapsedMinutes = (elapsedTime[0] /60)%60;
            long elapsedHour = (elapsedTime[0] /(60*60))%24;

            if(elapsedHour > 0)
                elapsedTimeFormat = String.format("%02d:%02d:%02d",elapsedHour,elapsedMinutes,elapsedSeconds);
            else
                elapsedTimeFormat = String.format("%02d:%02d",elapsedMinutes,elapsedSeconds);
            timeElapsed.setText(elapsedTimeFormat);
            songProgress.setProgress((int) elapsedTime[0]);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            elapsedTime[0]++;


        }*/
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
       /* if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
    }

    @Override
    public void onDetach() {
        super.onDetach();
       // mListener = null;
    }

    @Override
    public void onDestroy() {
       // super.onDestroy();
        Spotify.destroyPlayer(this);
        super.onDestroy();
    }

    @Override
    public void onPlaybackEvent(PlayerEvent playerEvent) {
        Log.d("MainActivity", "Playback event received: " + playerEvent);
        switch (playerEvent) {
            // Handle event type as necessary
            case kSpPlaybackNotifyPlay:
                playPauseButton.setImageResource(android.R.drawable.ic_media_pause);
                isPlaying = true;
                break;
            case kSpPlaybackNotifyTrackChanged:

            default:
                break;
        }
    }

    @Override
    public void onPlaybackError(Error error) {

    }

    @Override
    public void onLoggedIn() {
        player.playUri(songUrl,0,0);

    }

    @Override
    public void onLoggedOut() {

    }

    @Override
    public void onLoginFailed(int i) {

    }

    @Override
    public void onTemporaryError() {

    }

    @Override
    public void onConnectionMessage(String s) {

    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.playButton:
                if(isPlaying){
                    playPauseButton.setImageResource(android.R.drawable.ic_media_play);
                    isPlaying = false;
                    player.pause();
                }else {
                    playPauseButton.setImageResource(android.R.drawable.ic_media_pause);
                    player.resume();
                    isPlaying = true;
                }
                break;
            case R.id.next_song:
                songPosition = songPosition + 1;
                isNext = true;
                retrieveNextOrPreviousSongDetails();

                Log.d("MainActivity","playback next song button clicked");

                break;
            case R.id.previous_song:
                songPosition = songPosition -1;
                isPrevious = true;
                retrieveNextOrPreviousSongDetails();
                Log.d("MainActivity","playback previous song button clicked");
               /* songName.setText(nameOfSong+" - " + albumName);
                artistName.setText(artist);
                DecodeBitmap decodeBitmapPrevious = new DecodeBitmap(song_image_player,imageURL);
                decodeBitmapPrevious.execute();
                player.playUri(songUrl,0,0);*/
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    /*public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }*/
    class DecodeBitmap extends AsyncTask<Void,Void,Bitmap> {
        private WeakReference<ImageView> mImageViewWeakReerence = null;
        private String song;

        public DecodeBitmap(ImageView imageView, String song) {
            this.mImageViewWeakReerence = new WeakReference<ImageView>(imageView);
            this.song = song;
        }


        @Override
        protected Bitmap doInBackground(Void... voids) {
            return decodeUri(song);
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

            return output;
        }
    }
    public void updateSongDetails(String nameOfSong,String songUrl,String artist,String imageURL,String albumName){
        this.nameOfSong = nameOfSong;
        this.songUrl = songUrl;
        this.artist = artist;
        this.imageURL = imageURL;
        this.albumName = albumName;
        songName.setText(this.nameOfSong+" - " + albumName);
        artistName.setText(artist);
        DecodeBitmap decodeBitmapNext = new DecodeBitmap(song_image_player,imageURL);
        decodeBitmapNext.execute();
        player.playUri(songUrl,0,0);
        /**/

    }
    public void retrieveNextOrPreviousSongDetails(){
        if (playlistType.contains("my_song")) {
            Log.d("MainActivity", "playback song position now " + songPosition);
            checkIfFirstOrLastSong(DataService.getInstance().getUserSongsList());
            Songs song = DataService.getInstance().getUserSongsList().get(songPosition);
            updateSongDetails(song.getSongName(), song.getSongUri(), song.getArtistName(), song.getSongImageUrl(), song.getAlbumName());

        } else if (playlistType.split(",")[0].equalsIgnoreCase("user_playlists")) {
            checkIfFirstOrLastSong(DataService.getInstance().getUserPlaylistsTracksMap()
                    .get(playlistType.split(",")[1]));
            Songs song = DataService.getInstance().getUserPlaylistsTracksMap()
                    .get(playlistType.split(",")[1]).get(songPosition);

            updateSongDetails(song.getSongName(), song.getSongUri(), song.getArtistName(), song.getSongImageUrl(), song.getAlbumName());

        } else {
            checkIfFirstOrLastSong(DataService.getInstance().getUserPlaylistsTracksMap()
                    .get(playlistType.split(",")[1]));
            Songs song = DataService.getInstance().getUserPlaylistsTracksMap()
                    .get(playlistType.split(",")[1]).get(songPosition);

            updateSongDetails(song.getSongName(), song.getSongUri(), song.getArtistName(), song.getSongImageUrl(), song.getAlbumName());

        }
    }
    public void checkIfFirstOrLastSong(ArrayList<Songs> songsList){
        if(isNext){
            if(songPosition == songsList.size())
                songPosition = 0;
        }
        if(isPrevious){
            if(songPosition == -1)
                songPosition = songsList.size() -1;
        }
    }
}
