package com.oncelabs.blehero.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.oncelabs.blehero.databinding.FragmentSettingsBinding
import com.oncelabs.blehero.ui.viewmodels.SettingsViewModel
import com.oncelabs.blehero.R

class SettingsFragment : Fragment(){
    private val settingsViewModel: SettingsViewModel by lazy {
        ViewModelProvider(this).get(SettingsViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        val binding = FragmentSettingsBinding.bind(inflater.inflate(R.layout.fragment_settings, container, false))
        binding.viewModel = settingsViewModel
        return binding.root
    }
}