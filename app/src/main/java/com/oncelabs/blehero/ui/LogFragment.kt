package com.oncelabs.blehero.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.oncelabs.blehero.databinding.FragmentLogBinding
import com.oncelabs.blehero.ui.viewmodels.LogViewModel
import com.oncelabs.blehero.R

class LogFragment : Fragment(){
    private val logViewModel: LogViewModel by lazy {
        ViewModelProvider(this).get(LogViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        val binding = FragmentLogBinding.bind(inflater.inflate(R.layout.fragment_log, container, false))
        binding.viewModel = logViewModel
        return binding.root
    }
}