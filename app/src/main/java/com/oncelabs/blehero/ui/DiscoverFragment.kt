package com.oncelabs.blehero.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.oncelabs.blehero.databinding.FragmentDiscoverBinding
import com.oncelabs.blehero.ui.viewmodels.DiscoverViewModel
import com.oncelabs.blehero.R

class DiscoverFragment : Fragment(){
    private val discoverViewModel: DiscoverViewModel by lazy {
        ViewModelProvider(this).get(DiscoverViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        val binding = FragmentDiscoverBinding.bind(inflater.inflate(R.layout.fragment_discover, container, false))
        binding.viewModel = discoverViewModel
        return binding.root
    }
}