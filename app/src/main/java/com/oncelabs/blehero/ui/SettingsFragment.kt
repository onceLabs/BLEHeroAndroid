package com.oncelabs.blehero.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceFragmentCompat
import com.oncelabs.blehero.databinding.FragmentSettingsBinding
import com.oncelabs.blehero.ui.viewmodels.SettingsViewModel
import com.oncelabs.blehero.R

class SettingsFragment : PreferenceFragmentCompat() {

    private val settingsViewModel: SettingsViewModel by lazy {
        ViewModelProvider(this).get(SettingsViewModel::class.java)
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.app_settings, rootKey)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        view?.background = ContextCompat.getDrawable(requireContext(), R.color.colorPrimaryDark)
        return view;
    }

//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        super.onCreateView(inflater, container, savedInstanceState)
//
//        val binding = FragmentSettingsBinding.bind(inflater.inflate(R.layout.fragment_settings, container, false))
//        binding.viewModel = settingsViewModel
//        return binding.root
//    }
}