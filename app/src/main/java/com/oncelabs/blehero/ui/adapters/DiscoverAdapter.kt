package com.oncelabs.blehero.ui.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.oncelabs.blehero.R
import com.oncelabs.blehero.databinding.ListDiscoveredDeviceBinding
import com.oncelabs.blehero.model.Device
import com.oncelabs.blehero.ui.adapters.holders.DiscoverViewHolder


class DiscoverAdapter : RecyclerView.Adapter<DiscoverViewHolder>(){

    private val devices = mutableListOf<Device>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiscoverViewHolder {

        return DiscoverViewHolder(
            ListDiscoveredDeviceBinding.bind(
            LayoutInflater.from(parent.context).inflate(
            R.layout.list_discovered_device,
            parent,
            false
        )))
    }

    override fun onBindViewHolder(holder: DiscoverViewHolder, position: Int) {
        holder.bind(devices[position])
    }

    override fun getItemCount(): Int {
        Log.i("DiscoverAdapter", devices.size.toString())
        return devices.size
    }

    fun addDevice(device: Device){
        devices.add(0, device)
        notifyDataSetChanged()
    }
}