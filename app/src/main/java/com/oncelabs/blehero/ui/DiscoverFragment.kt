package com.oncelabs.blehero.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.oncelabs.blehero.databinding.FragmentDiscoverBinding
import com.oncelabs.blehero.ui.viewmodels.DiscoverViewModel
import com.oncelabs.blehero.R
import com.oncelabs.blehero.model.Device
import com.oncelabs.blehero.model.DeviceManager
import com.oncelabs.blehero.ui.adapters.DiscoverAdapter
import com.oncelabs.onceble.core.peripheral.OBPeripheral
import kotlinx.android.synthetic.main.fragment_discover.*
import kotlinx.android.synthetic.main.list_discovered_device.*

class DiscoverFragment : Fragment(){

//    private val discoverViewModel: DiscoverViewModel by lazy {
//        ViewModelProvider(this).get(DiscoverViewModel::class.java)
//    }

    lateinit var binding: FragmentDiscoverBinding
    private var adapter : DiscoverAdapter? = null

    private val discoverViewModel: DiscoverViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentDiscoverBinding.bind(inflater.inflate(R.layout.fragment_discover, container, false))

        discoverViewModel.init(viewLifecycleOwner)
        binding.viewModel = discoverViewModel

        initList()
        bindObservers()
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    private fun bindObservers(){
        discoverViewModel.filteredDevices.observe(viewLifecycleOwner, Observer{
            adapter?.updateDevices(it)
            discovered_devices_number.text = "${it.size} Devices"
        })

        binding.discoveredDeviceSearch.setOnClickListener {
            print("Clicked search button")
            if(binding.deviceSearchContainer.visibility == View.GONE){
                binding.deviceSearchContainer.visibility = View.VISIBLE
            }
            else{
                binding.deviceSearchContainer.visibility = View.GONE
                binding.searchField.text.clear()
            }
        }

        binding.cancelSearchButton.setOnClickListener {
            binding.deviceSearchContainer.visibility = View.GONE
            binding.searchField.text.clear()
        }

        binding.filterSortButton.setOnClickListener {
            showFilterSortDialog()
//            showGattActivity()
        }

        binding.searchField.doOnTextChanged { text, start, before, count ->
            discoverViewModel.discoverFilter.searchString = text.toString()
            discoverViewModel.updateDevices()
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

    private fun showFilterSortDialog() {
        val filterSortDialog = DiscoveryFilterFragment();
        childFragmentManager.let{
            filterSortDialog.show(it, "filterSortDialog")
        }
    }

    private fun showGattActivity(){
        val intent = Intent(context, GattProfileActivity::class.java)
        startActivity(intent)
    }
}