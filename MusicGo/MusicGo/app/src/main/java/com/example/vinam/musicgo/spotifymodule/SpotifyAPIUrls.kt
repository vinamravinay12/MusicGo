package com.example.vinam.musicgo.spotifymodule

import com.example.vinam.musicgo.spotifymodule.models.SpotifyUser
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface SpotifyAPIUrls {

    @GET("/me")
    fun getUser(@Header("Authorization") authKey:String, @Query("scope") scope: Array<String>) : Call<SpotifyUser>



}