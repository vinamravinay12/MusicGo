package com.example.vinam.musicgo.fragments

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.vinam.musicgo.R

class ProfleFragment : Fragment() {

    companion object {
        fun newInstance() = ProfleFragment()
    }

    private lateinit var viewModel: ProfleViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.profle_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ProfleViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
