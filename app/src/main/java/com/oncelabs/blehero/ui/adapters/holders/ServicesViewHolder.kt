package com.oncelabs.blehero.ui.adapters.holders

import androidx.recyclerview.widget.RecyclerView
import com.oncelabs.blehero.databinding.ServiceListItemBinding
import com.oncelabs.onceble.core.peripheral.OBPeripheral
import com.oncelabs.onceble.core.peripheral.gattClient.OBGatt
import com.oncelabs.onceble.core.peripheral.gattClient.OBService


class ServicesViewHolder(val binding: ServiceListItemBinding) : RecyclerView.ViewHolder(binding.root){

    private lateinit var _obService: OBService

    fun bind(obService: OBService){
        binding.service = _obService

    }
}