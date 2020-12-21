package com.oncelabs.blehero.ui.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.oncelabs.blehero.R
import com.oncelabs.blehero.databinding.ListDiscoveredDeviceBinding
import com.oncelabs.blehero.model.Device
import com.oncelabs.blehero.ui.adapters.holders.DiscoverViewHolder
import com.oncelabs.onceble.core.peripheral.OBPeripheral


class DiscoverAdapter() : RecyclerView.Adapter<DiscoverViewHolder>(){

    private val devices = mutableListOf<OBPeripheral>()

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
        println("Binding viewholder")
    }

    override fun getItemCount(): Int {
        Log.i("DiscoverAdapter", devices.size.toString())
        return devices.size
    }

    fun addDevice(obPeripheral: OBPeripheral){
        if(!devices.contains(obPeripheral)){
            println("Adding device ${obPeripheral.latestAdvData.value?.name}")
            devices.add(0, obPeripheral)
            notifyDataSetChanged()
        }
    }
}