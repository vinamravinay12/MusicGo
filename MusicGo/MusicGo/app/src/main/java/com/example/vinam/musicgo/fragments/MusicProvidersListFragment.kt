package com.example.vinam.musicgo.fragments

import android.content.Intent
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
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
import com.google.android.material.snackbar.Snackbar
import com.spotify.sdk.android.authentication.AuthenticationClient
import com.spotify.sdk.android.authentication.AuthenticationRequest
import com.spotify.sdk.android.authentication.AuthenticationResponse

class MusicProvidersListFragment : Fragment(), View.OnClickListener {

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
        val builder = AuthenticationRequest.Builder(getString(R.string.SPOTIFY_CLIENT_ID),AuthenticationResponse.Type.TOKEN,SpotifyConstants.REDIRECT_URI)
        builder.setScopes(arrayOf("streaming"))
        val authenticationRequest = builder.build()
        AuthenticationClient.openLoginActivity(activity,SpotifyConstants.SPOTIFY_AUTHENTICATION_REQUEST_ID,authenticationRequest)


    }


    override fun onClick(v: View?) {
       connectToSpotify()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (requestCode == SpotifyConstants.SPOTIFY_AUTHENTICATION_REQUEST_ID) {

            val response = AuthenticationClient.getResponse(resultCode,data)

            when(response.type) {
                AuthenticationResponse.Type.TOKEN -> viewModel.accessToken = response.accessToken
                AuthenticationResponse.Type.ERROR -> showErrorMessage(response.error)
            }


        }
    }

    private fun showErrorMessage(errorMessage : String) {
        view?.let { Snackbar.make(it, errorMessage, Snackbar.LENGTH_LONG) }?.show()

    }



}
