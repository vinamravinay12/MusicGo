package com.example.vinam.musicgo.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.vinam.musicgo.R
import com.example.vinam.musicgo.fragments.MusicProvidersListFragment
import com.example.vinam.musicgo.spotifymodule.SpotifyConstants
import com.spotify.sdk.android.authentication.AuthenticationClient
import com.spotify.sdk.android.authentication.AuthenticationResponse

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction().add(R.id.main_frame,MusicProvidersListFragment.newInstance()).commit()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SpotifyConstants.SPOTIFY_AUTHENTICATION_REQUEST_ID) {

            val response = AuthenticationClient.getResponse(resultCode, data)

//            when(response.type) {
//                AuthenticationResponse.Type.TOKEN -> viewModel.accessToken = response.accessToken
//                AuthenticationResponse.Type.ERROR -> showErrorMessage(response.error)
//            }


        }

    }
}



