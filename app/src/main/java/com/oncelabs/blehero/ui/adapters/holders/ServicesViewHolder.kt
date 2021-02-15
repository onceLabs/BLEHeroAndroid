package com.oncelabs.blehero.ui.adapters.holders

import android.bluetooth.BluetoothGattService
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.oncelabs.blehero.databinding.ServiceListItemBinding
import com.oncelabs.blehero.ui.adapters.GattCharacteristicsAdapter
import com.oncelabs.blehero.ui.adapters.GattServicesAdapter
import com.oncelabs.onceble.core.peripheral.OBPeripheral
import com.oncelabs.onceble.core.peripheral.gattClient.OBCharacteristic
import com.oncelabs.onceble.core.peripheral.gattClient.OBGatt
import com.oncelabs.onceble.core.peripheral.gattClient.OBService


class ServicesViewHolder(val binding: ServiceListItemBinding) : RecyclerView.ViewHolder(binding.root){

    private var _obService: OBService? = null
    private var adapter : GattCharacteristicsAdapter? = null

    fun bind(obService: OBService){
        _obService = obService
        binding.service = obService
        println("Bound service = ${_obService?.uuid}")

        initList()
        bindListeners()
    }

    private fun bindListeners(){
        binding.root.setOnClickListener {
            if(binding.characteristicList.visibility == View.GONE){
                binding.characteristicList.visibility = View.VISIBLE
            }
            else{
                binding.characteristicList.visibility = View.GONE
            }
        }
    }

    private fun initList() {
        if(adapter == null) {
            adapter = GattCharacteristicsAdapter()
            binding.characteristicList.layoutManager = LinearLayoutManager(binding.root.context)
            binding.characteristicList.adapter = adapter

            val tempList = mutableListOf<OBCharacteristic>()
            _obService?.characteristics?.forEach {
                tempList.add(it)
            }

            adapter?.updateCharacteristics(tempList)
        } else {
            //adapter?.updateCallback(this)
        }
    }
}