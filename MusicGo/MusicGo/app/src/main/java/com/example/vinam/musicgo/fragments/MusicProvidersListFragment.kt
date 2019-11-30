package com.example.vinam.musicgo.fragments

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick

import com.example.vinam.musicgo.R
import com.example.vinam.musicgo.spotifymodule.SpotifyConstants
import com.spotify.android.appremote.api.ConnectionParams
import com.spotify.android.appremote.api.Connector
import com.spotify.android.appremote.api.SpotifyAppRemote

class MusicProvidersListFragment : Fragment(), Connector.ConnectionListener, View.OnClickListener {

    companion object {
        fun newInstance() = MusicProvidersListFragment()
    }

    @BindView(R.id.connect_spotify) lateinit var spotifyBtn : Button;
    private lateinit var viewModel: MusicProvidersListViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view =  inflater.inflate(R.layout.music_providers_list_fragment, container, false)
        ButterKnife.bind(this,view)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(MusicProvidersListViewModel::class.java)
       // spotifyBtn.setOnClickListener(this)


    }


    @OnClick(R.id.connect_spotify)
    fun connectToSpotify() {
        val connectionParams : ConnectionParams =  ConnectionParams.Builder(getString(R.string.SPOTIFY_CLIENT_ID)).setRedirectUri(SpotifyConstants.REDIRECT_URI).showAuthView(true).build()


        SpotifyAppRemote.connect(context,connectionParams,this)
    }


    override fun onClick(v: View?) {
       connectToSpotify()
    }






    override fun onFailure(throwable: Throwable?) {
        Log.d("TAG","error: " + (throwable?.message ?: "Error"))
    }

    override fun onConnected(appRemote:  SpotifyAppRemote?) {
        viewModel.spotifyAppRemote = appRemote
        Log.d("TAG","Connected")

    }

}
