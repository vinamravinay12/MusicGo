package com.example.vinam.musicgo.fragments

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.vinam.musicgo.R

class PlaylistDetailsFragment : Fragment() {

    companion object {
        fun newInstance() = PlaylistDetailsFragment()
    }

    private lateinit var viewModel: PlaylistDetailsViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.playlist_details_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(PlaylistDetailsViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
