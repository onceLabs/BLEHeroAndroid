package com.oncelabs.blehero.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.oncelabs.blehero.R
import com.oncelabs.blehero.databinding.FragmentDiscoverBinding
import com.oncelabs.blehero.model.Device
import com.oncelabs.blehero.ui.adapters.DiscoverAdapter
import com.oncelabs.blehero.ui.viewmodels.DiscoverViewModel
import com.oncelabs.oncebleandroid.BuildConfig

class DiscoverFragment : Fragment(){

    private val discoverViewModel: DiscoverViewModel by lazy {
        ViewModelProvider(this).get(DiscoverViewModel::class.java)
    }

    lateinit var binding: FragmentDiscoverBinding
    private var adapter : DiscoverAdapter? = null
    private var bleCentral: OBCentralManager? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        binding = FragmentDiscoverBinding.bind(inflater.inflate(R.layout.fragment_discover, container, false))
        binding.viewModel = discoverViewModel
        initList()
        bindObservers()
        return binding.root
    }

    private fun bindObservers(){
        adapter?.addDevice(Device())
        adapter?.addDevice(Device())
        adapter?.addDevice(Device())
        adapter?.addDevice(Device())

        binding.discoveredDeviceSearch.setOnClickListener {
            print("Clicked search button")
            if(binding.deviceSearchContainer.visibility == View.GONE){
                binding.deviceSearchContainer.visibility = View.VISIBLE
            }
            else{
                binding.deviceSearchContainer.visibility = View.GONE
            }
        }

        binding.cancelSearchButton.setOnClickListener {
            binding.deviceSearchContainer.visibility = View.GONE
        }
    }

    override fun onResume() {
        super.onResume()
        initList()
    }

    private fun initList() {
        if(adapter == null) {
            adapter = DiscoverAdapter()
            binding.fragmentDiscoverList.layoutManager = LinearLayoutManager(context)
            binding.fragmentDiscoverList.adapter = adapter


        } else {
            //adapter?.updateCallback(this)
        }
    }
}