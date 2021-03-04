package com.oncelabs.blehero.ui.adapters.holders

import android.bluetooth.BluetoothGattService
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.oncelabs.blehero.databinding.ServiceListItemBinding
import com.oncelabs.blehero.interfaces.GattInterface
import com.oncelabs.blehero.ui.adapters.GattCharacteristicsAdapter
import com.oncelabs.blehero.ui.adapters.GattServicesAdapter
import com.oncelabs.onceble.core.peripheral.OBPeripheral
import com.oncelabs.onceble.core.peripheral.gattClient.OBCharacteristic
import com.oncelabs.onceble.core.peripheral.gattClient.OBGatt
import com.oncelabs.onceble.core.peripheral.gattClient.OBService
import kotlin.math.exp


class ServicesViewHolder(val binding: ServiceListItemBinding) : RecyclerView.ViewHolder(binding.root){

    private var _obService: OBService? = null
    private var adapter : GattCharacteristicsAdapter? = null
    private var gattInterface: GattInterface? = null

    private var itemChanged: ((Boolean)->Unit)? = null

    private var expanded = false

    fun bind(obService: OBService, expanded: Boolean, gattInterface: GattInterface){
        this.gattInterface = gattInterface
        this.expanded = expanded
        _obService = obService
        binding.service = obService
        println("Bound service = ${_obService?.uuid}")

        initList()
        bindListeners()
        initView()
    }

    fun onItemChanged(callback: ((Boolean)->Unit)){
        itemChanged = callback
    }

    fun initView(){
        if(expanded){
            binding.characteristicList.visibility = View.VISIBLE
        }
        else{
            binding.characteristicList.visibility = View.GONE
        }
    }

    private fun bindListeners(){
        binding.root.setOnClickListener {
            itemChanged?.invoke(!expanded)
        }
    }

    private fun initList() {
//        if(adapter == null) {
        if(true){
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