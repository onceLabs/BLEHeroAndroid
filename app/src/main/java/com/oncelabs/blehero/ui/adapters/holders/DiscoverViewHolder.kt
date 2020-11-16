package com.oncelabs.blehero.ui.adapters.holders

import androidx.recyclerview.widget.RecyclerView
import com.oncelabs.blehero.databinding.ListDiscoveredDeviceBinding
import com.oncelabs.blehero.model.Device

class DiscoverViewHolder(val binding: ListDiscoveredDeviceBinding) : RecyclerView.ViewHolder(binding.root){
    fun bind(device: Device){
        binding.device = device
    }
}