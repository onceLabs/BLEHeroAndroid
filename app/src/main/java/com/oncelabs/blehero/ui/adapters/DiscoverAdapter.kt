package com.oncelabs.blehero.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.oncelabs.blehero.R
import com.oncelabs.blehero.databinding.ListDiscoveredDeviceBinding
import com.oncelabs.blehero.ui.adapters.holders.DiscoverViewHolder
import com.oncelabs.onceble.core.peripheral.OBPeripheral
import com.oncelabs.onceble.core.peripheral.gattClient.OBGatt


class DiscoverAdapter() : RecyclerView.Adapter<DiscoverViewHolder>(){

    private val devices = mutableListOf<OBPeripheral<out OBGatt>>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiscoverViewHolder {

        return DiscoverViewHolder(
            ListDiscoveredDeviceBinding.bind(
            LayoutInflater.from(parent.context).inflate(
            R.layout.list_discovered_device,
            parent,
            false
        )))
    }

    override fun onViewDetachedFromWindow(holder: DiscoverViewHolder) {
        super.onViewDetachedFromWindow(holder)
        holder.dispose()
    }

    override fun onViewAttachedToWindow(holder: DiscoverViewHolder) {
        super.onViewAttachedToWindow(holder)
        holder.initializeBindings()
//        println("onViewAttached ${holder.binding.deviceName}")
    }

    override fun onBindViewHolder(holder: DiscoverViewHolder, position: Int) {
//        holder.setIsRecyclable(false)
        holder.bind(devices[position])
//        println("Binding viewholder")
    }

    override fun getItemCount(): Int {
//        Log.i("DiscoverAdapter", devices.size.toString())
        return devices.size
    }

    fun addDevice(obPeripheral: OBPeripheral<out OBGatt>){
        if(!devices.contains(obPeripheral)){
            println("Adding device ${obPeripheral.latestAdvData.value?.name}")
            devices.add(0, obPeripheral)
            notifyDataSetChanged()
        }
    }

    fun updateDevices(devicesList: List<OBPeripheral<out OBGatt>>){
        val addedDevices: MutableList<OBPeripheral<out OBGatt>> = mutableListOf()
        val removedDevices: MutableList<OBPeripheral<out OBGatt>> = mutableListOf()

        //Check for devices that have been removed
        devices.forEach {
            if(!devicesList.contains(it)){
                removedDevices.add(it)
            }
        }

        //Check for devices that have been added
        devicesList.forEach {
            if(!devices.contains(it)){
                addedDevices.add(it)
            }
        }

        //Remove the devices
        removedDevices.forEach {
            val index = devices.indexOf(it)
            devices.remove(it)
            notifyItemRemoved(index)
        }

        //Add the devices
        addedDevices.forEach {
            devices.add(it)
            notifyItemInserted(devices.lastIndex)
        }
    }
}