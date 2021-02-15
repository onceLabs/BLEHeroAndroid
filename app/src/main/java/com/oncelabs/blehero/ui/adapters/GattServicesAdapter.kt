package com.oncelabs.blehero.ui.adapters

import android.bluetooth.BluetoothGattService
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.oncelabs.blehero.R
import com.oncelabs.blehero.databinding.ListDiscoveredDeviceBinding
import com.oncelabs.blehero.databinding.ServiceListItemBinding
import com.oncelabs.blehero.ui.adapters.holders.DiscoverViewHolder
import com.oncelabs.blehero.ui.adapters.holders.ServicesViewHolder
import com.oncelabs.onceble.core.peripheral.OBPeripheral
import com.oncelabs.onceble.core.peripheral.gattClient.OBGatt
import com.oncelabs.onceble.core.peripheral.gattClient.OBService
import java.util.*


class GattServicesAdapter() : RecyclerView.Adapter<ServicesViewHolder>(){

    private val services: MutableList<OBService> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServicesViewHolder {

        return ServicesViewHolder(
            ServiceListItemBinding.bind(
            LayoutInflater.from(parent.context).inflate(
            R.layout.service_list_item,
            parent,
            false
        )))
    }

    override fun onBindViewHolder(holder: ServicesViewHolder, position: Int) {
        holder.bind(services[position])
    }

    override fun getItemCount(): Int {
        return services.size
    }

    fun updateServices(serviceList: List<OBService>){
        if(services != serviceList){
            services.clear()
            serviceList.forEach{
                services.add(it)
            }

            println("Services changed")
            notifyDataSetChanged()
        }
    }
}