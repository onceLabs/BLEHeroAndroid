package com.oncelabs.blehero.ui.adapters.holders

import android.util.DisplayMetrics
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.oncelabs.blehero.databinding.ListDiscoveredDeviceBinding
import com.oncelabs.blehero.model.Device


class DiscoverViewHolder(val binding: ListDiscoveredDeviceBinding) : RecyclerView.ViewHolder(binding.root){
    fun bind(device: Device){
        binding.device = device

        //This value needs to be calculated in realtime based on screen width
        val translationDistance = 820f

        binding.actionInfoButton.setOnClickListener {
            if(binding.infoContainer.translationX == 0f){
                binding.actionsContainer.translationX = translationDistance
                binding.actionsContainer.animate().translationX(0f)
                binding.infoContainer.animate().translationX(translationDistance * (-1))
                binding.actionInfoButton.text = "Info"
            }
            else {
                binding.infoContainer.animate().translationX(0f)
                binding.actionsContainer.animate().translationX(translationDistance)
                binding.actionInfoButton.text = "Actions"
            }
        }

    }
}