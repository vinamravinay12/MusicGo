package com.example.vinam.musicgo.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Rect;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
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
import com.spotify.sdk.android.player.PlaybackState;
import com.spotify.sdk.android.player.Player;
import com.spotify.sdk.android.player.PlayerEvent;
import com.spotify.sdk.android.player.Spotify;
import com.spotify.sdk.android.player.SpotifyPlayer;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Random;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 *
 * to handle interaction events.
 * Use the {@link SpotifyPlayerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SpotifyPlayerFragment extends Fragment implements View.OnClickListener,SpotifyPlayer.NotificationCallback,ConnectionStateCallback,SeekBar.OnSeekBarChangeListener {
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
    int repeatClicked;
    boolean isRepeat;
    boolean isShuffled;
    boolean isRepeatOneSong;
    boolean isLastSong;
    private Handler mHandler = new Handler();
    private long seekForwardTime = 5000;
    private long seekBackwardTime = 5000;
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
        duration = Long.parseLong(b.getString("song_duration"));
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
        shuffleButton.setOnClickListener(this);
        repeatButton.setOnClickListener(this);
        songProgress.setOnSeekBarChangeListener(this);
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
        timeElapsed.setText("0:00");
        totalTime.setText("mm:ss");
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
       // mListener = null;
    }

    @Override
    public void onDestroy() {
       // super.onDestroy();
       // Spotify.destroyPlayer(this);
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
           // case kSpPlaybackNotifyTrackChanged:
            case kSpPlaybackNotifyAudioDeliveryDone:
                if(isRepeat){
                    songPosition++;
                    retrieveNextOrPreviousSongDetails();

                } else if(isRepeatOneSong){
                    player.playUri(songUrl,0,0);
                } else if(isShuffled){
                    songPosition = getRandomSongPosition();
                    retrieveNextOrPreviousSongDetails();
                } else {
                    songPosition = songPosition + 1;
                    isNext = false;
                    retrieveNextOrPreviousSongDetails();
                }

                break;
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
        updateProgressBar();
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
                if(isShuffled)
                    songPosition = getRandomSongPosition();
                else songPosition = songPosition + 1;
                isNext = true;
                if(isRepeatOneSong){
                    isRepeatOneSong = false;
                    isRepeat = true;
                    repeatClicked =1;
                    repeatButton.setImageResource(R.drawable.ic_repeat_white_24dp);
                }
                retrieveNextOrPreviousSongDetails();
              //  player.skipToNext();
                Log.d("MainActivity","playback next song button clicked");

                break;
            case R.id.previous_song:
                if(isShuffled)
                    songPosition = getRandomSongPosition();
                else songPosition = songPosition -1;
                isPrevious = true;
                if(isRepeatOneSong){
                    isRepeatOneSong = false;
                    isRepeat = true;
                    repeatClicked =1;
                    repeatButton.setImageResource(R.drawable.ic_repeat_white_24dp);
                }
                retrieveNextOrPreviousSongDetails();
                Log.d("MainActivity","playback previous song button clicked");
                break;
            case R.id.repeat_song:
                if(repeatClicked == 0){
                    repeatButton.setColorFilter(Color.GREEN);
                    isRepeat = true;
                    isRepeatOneSong = false;
                    repeatClicked = 1;
                }else if(repeatClicked == 1){
                    repeatButton.setImageResource(R.drawable.ic_repeat_one_white_24dp);
                    repeatButton.setColorFilter(Color.GREEN);
                    isRepeat = false;
                    isRepeatOneSong = true;
                    isShuffled = false;
                    repeatClicked =2;

                }else{
                    repeatButton.setImageResource(R.drawable.ic_repeat_white_24dp);
                    repeatButton.setColorFilter(Color.WHITE);
                    isRepeatOneSong = false;
                    repeatClicked = 0;
                }
                break;
            case R.id.shuffle_song:
                shuffleButton.setColorFilter(Color.GREEN);
                if(isShuffled) {
                    isShuffled = false;
                    shuffleButton.setColorFilter(Color.WHITE);
                }else {
                    isShuffled = true;

                }
                break;
            case R.id.forwardButton:
                long currentPosition = player.getPlaybackState().positionMs;
                if(currentPosition + seekForwardTime <= duration){
                    player.seekToPosition((int) (currentPosition+seekForwardTime));
                }else{
                    player.seekToPosition((int) duration);
                }
                break;
            case R.id.rewindButton:
                long currentPositionInMs = player.getPlaybackState().positionMs;
                if(currentPositionInMs - seekBackwardTime >=0){
                    player.seekToPosition((int) (currentPositionInMs - seekBackwardTime));
                }else{
                    player.seekToPosition(0);
                }
               /* songName.setText(nameOfSong+" - " + albumName);
                artistName.setText(artist);
                DecodeBitmap decodeBitmapPrevious = new DecodeBitmap(song_image_player,imageURL);
                decodeBitmapPrevious.execute();
                player.playUri(songUrl,0,0);*/
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        mHandler.removeCallbacks(updateTimeTask);
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        mHandler.removeCallbacks(updateTimeTask);
        int totalDuration = (int) duration;
        int currentPosition = progressToTimer(seekBar.getProgress(), totalDuration);

        // forward or backward to certain seconds
       // player.seekTo(currentPosition);
        player.seekToPosition(currentPosition);

        // update timer progress again
        updateProgressBar();
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
    public void updateSongDetails(String nameOfSong,String songUrl,String artist,String imageURL,String albumName,long duration){
        player.refreshCache();
        this.nameOfSong = nameOfSong;
        this.songUrl = songUrl;
        this.artist = artist;
        this.imageURL = imageURL;
        this.albumName = albumName;
        this.duration = duration;
        songName.setText(this.nameOfSong+" - " + albumName);
        artistName.setText(artist);
        DecodeBitmap decodeBitmapNext = new DecodeBitmap(song_image_player,imageURL);
        decodeBitmapNext.execute();
        timeElapsed.setText("0:00");

        if(isLastSong && !isRepeat) {
            player.pause();
            playPauseButton.setImageResource(android.R.drawable.ic_media_play);
            isLastSong = false;
        }
        else {
            player.playUri(songUrl,0,0);
            updateProgressBar();
        }

        /**/

    }
    public void retrieveNextOrPreviousSongDetails(){
        if (playlistType.contains("my_song")) {
            Log.d("MainActivity", "playback song position now " + songPosition);
            checkIfFirstOrLastSong(DataService.getInstance().getUserSongsList());
            Songs song = DataService.getInstance().getUserSongsList().get(songPosition);
            updateSongDetails(song.getSongName(), song.getSongUri(), song.getArtistName(), song.getSongImageUrl(), song.getAlbumName(), Long.parseLong(song.getSongDuration()));

        } else if (playlistType.split(",")[0].equalsIgnoreCase("user_playlists")) {
            checkIfFirstOrLastSong(DataService.getInstance().getUserPlaylistsTracksMap()
                    .get(playlistType.split(",")[1]));
            Songs song = DataService.getInstance().getUserPlaylistsTracksMap()
                    .get(playlistType.split(",")[1]).get(songPosition);

            updateSongDetails(song.getSongName(), song.getSongUri(), song.getArtistName(), song.getSongImageUrl(), song.getAlbumName(),Long.parseLong(song.getSongDuration()));

        } else {
            checkIfFirstOrLastSong(DataService.getInstance().getFeaturedPlaylistsTracksMap()
                    .get(playlistType.split(",")[1]));
            Songs song = DataService.getInstance().getFeaturedPlaylistsTracksMap()
                    .get(playlistType.split(",")[1]).get(songPosition);

            updateSongDetails(song.getSongName(), song.getSongUri(), song.getArtistName(), song.getSongImageUrl(), song.getAlbumName(),Long.parseLong(song.getSongDuration()));

        }
    }
    public void checkIfFirstOrLastSong(ArrayList<Songs> songsList){

            if(songPosition == songsList.size()) {
                songPosition = 0;
                isLastSong = true;
            } else if(songPosition == -1)
                if(isRepeat)
                    songPosition = songsList.size() -1;
                else songPosition = 0;


    }
    public int getRandomSongPosition(){
        int randomPosition = 0;
        Random random = new Random();
        if (playlistType.contains("my_song"))

            randomPosition = random.nextInt((DataService.getInstance().getUserSongsList().size() - 1) - 0 + 1);
        else if (playlistType.split(",")[0].equalsIgnoreCase("user_playlists"))
            randomPosition = random.nextInt((DataService.getInstance().getUserPlaylistsTracksMap()
                    .get(playlistType.split(",")[1]).size() - 1) - 0 + 1);

        else
            randomPosition = random.nextInt((DataService.getInstance().getFeaturedPlaylistsTracksMap()
                    .get(playlistType.split(",")[1]).size() - 1) - 0 + 1);


        return randomPosition;
    }
    public String milliSecondsToTimer(long milliseconds){
        String finalTimerString = "";
        String secondsString = "";

        // Convert total duration into time
        int hours = (int)( milliseconds / (1000*60*60));
        int minutes = (int)(milliseconds % (1000*60*60)) / (1000*60);
        int seconds = (int) ((milliseconds % (1000*60*60)) % (1000*60) / 1000);
        // Add hours if there
        if(hours > 0){
            finalTimerString = hours + ":";
        }

        // Prepending 0 to seconds if it is one digit
        if(seconds < 10){
            secondsString = "0" + seconds;
        }else{
            secondsString = "" + seconds;}

        finalTimerString = finalTimerString + minutes + ":" + secondsString;

        // return timer string
        return finalTimerString;
    }

    private Runnable updateTimeTask = new Runnable(){
        @Override
        public void run() {
           long totalDuration = duration;
            long currentDuration = player.getPlaybackState().positionMs;
            totalTime.setText(""+milliSecondsToTimer(totalDuration));
            timeElapsed.setText(""+milliSecondsToTimer(currentDuration));
            int progress = (int)(getProgressPercentage(currentDuration, totalDuration));
            songProgress.setProgress(progress);
            mHandler.postDelayed(this,100);
        }


    };

    public void updateProgressBar() {
        mHandler.postDelayed(updateTimeTask, 100);
    }

    public int getProgressPercentage(long currentDuration, long totalDuration){
        Double percentage = (double) 0;

        long currentSeconds = (int) (currentDuration / 1000);
        long totalSeconds = (int) (totalDuration / 1000);

        // calculating percentage
        percentage =(((double)currentSeconds)/totalSeconds)*100;

        // return percentage
        return percentage.intValue();
    }
    public int progressToTimer(int progress, int totalDuration) {
        int currentDuration = 0;
        totalDuration = (int) (totalDuration / 1000);
        currentDuration = (int) ((((double)progress) / 100) * totalDuration);

        // return current duration in milliseconds
        return currentDuration * 1000;
    }

}
