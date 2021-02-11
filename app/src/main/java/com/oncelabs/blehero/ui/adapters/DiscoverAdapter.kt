package com.oncelabs.blehero.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.oncelabs.blehero.R
import com.oncelabs.blehero.databinding.ListDiscoveredDeviceBinding
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

    fun addDevice(obPeripheral: OBPeripheral){
        if(!devices.contains(obPeripheral)){
            println("Adding device ${obPeripheral.latestAdvData.value?.name}")
            devices.add(0, obPeripheral)
            notifyDataSetChanged()
        }
    }

    fun updateDevices(devicesList: List<OBPeripheral>){
        devices.clear()
        devicesList.forEach{
            devices.add(it)
        }
        notifyDataSetChanged()
    }
}