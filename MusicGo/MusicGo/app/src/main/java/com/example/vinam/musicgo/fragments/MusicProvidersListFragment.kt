package com.example.vinam.musicgo.fragments

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.vinam.musicgo.R

class MusicProvidersListFragment : Fragment() {

    companion object {
        fun newInstance() = MusicProvidersListFragment()
    }

    private lateinit var viewModel: MusicProvidersListViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.music_providers_list_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(MusicProvidersListViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
