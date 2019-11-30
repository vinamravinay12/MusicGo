package com.example.vinam.musicgo.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.vinam.musicgo.R
import com.example.vinam.musicgo.fragments.MusicProvidersListFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction().add(R.id.main_frame,MusicProvidersListFragment.newInstance()).commit()
    }
}
