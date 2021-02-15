package com.oncelabs.blehero.ui.adapters

import android.bluetooth.BluetoothGattService
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.oncelabs.blehero.R
import com.oncelabs.blehero.databinding.CharacteristicListItemBinding
import com.oncelabs.blehero.databinding.ListDiscoveredDeviceBinding
import com.oncelabs.blehero.databinding.ServiceListItemBinding
import com.oncelabs.blehero.ui.adapters.holders.CharacteristicsViewHolder
import com.oncelabs.blehero.ui.adapters.holders.DiscoverViewHolder
import com.oncelabs.blehero.ui.adapters.holders.ServicesViewHolder
import com.oncelabs.onceble.core.peripheral.OBPeripheral
import com.oncelabs.onceble.core.peripheral.gattClient.OBCharacteristic
import com.oncelabs.onceble.core.peripheral.gattClient.OBGatt
import com.oncelabs.onceble.core.peripheral.gattClient.OBService
import java.util.*


class GattCharacteristicsAdapter() : RecyclerView.Adapter<CharacteristicsViewHolder>(){

    private val characteristics: MutableList<OBCharacteristic> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacteristicsViewHolder {

        return CharacteristicsViewHolder(
            CharacteristicListItemBinding.bind(
            LayoutInflater.from(parent.context).inflate(
            R.layout.characteristic_list_item,
            parent,
            false
        )))
    }

    override fun onBindViewHolder(holder: CharacteristicsViewHolder, position: Int) {
        holder.bind(characteristics[position])
    }

    override fun getItemCount(): Int {
        return characteristics.size
    }

    fun updateCharacteristics(characteristicList: List<OBCharacteristic>){
        if(characteristics != characteristicList){
            characteristics.clear()
            characteristicList.forEach{
                characteristics.add(it)
            }

            println("Characteristics changed")
            notifyDataSetChanged()
        }
    }
}