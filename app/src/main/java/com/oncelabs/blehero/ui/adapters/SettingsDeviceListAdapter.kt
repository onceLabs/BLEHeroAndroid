package com.oncelabs.blehero.ui.adapters

import android.bluetooth.BluetoothGattService
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.oncelabs.blehero.R
import com.oncelabs.blehero.databinding.SettingsDeviceListItemBinding
import com.oncelabs.blehero.model.SavedDevice
import com.oncelabs.blehero.ui.adapters.holders.SavedDeviceType
import com.oncelabs.blehero.ui.adapters.holders.SettingsDeviceViewHolder


class SettingsDeviceListAdapter() : RecyclerView.Adapter<SettingsDeviceViewHolder>(){

    private val devices: MutableList<SavedDevice> = mutableListOf()

    private var devicesType: SavedDeviceType? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SettingsDeviceViewHolder {

        return SettingsDeviceViewHolder(
            SettingsDeviceListItemBinding.bind(
            LayoutInflater.from(parent.context).inflate(
            R.layout.settings_device_list_item,
            parent,
            false
        )))
    }

    override fun onBindViewHolder(holder: SettingsDeviceViewHolder, position: Int) {
        devicesType?.let{
            holder.bind(devices[position], savedDeviceType = it)
        }
    }

    override fun getItemCount(): Int {
        return devices.size
    }

    fun updateDevices(deviceList: List<SavedDevice>, devicesType: SavedDeviceType){
        this.devicesType = devicesType
        if(devices != deviceList){
            devices.clear()
            deviceList.forEach{
                devices.add(it)
            }

            notifyDataSetChanged()
        }
    }
}