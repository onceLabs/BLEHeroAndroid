package com.oncelabs.blehero.ui.adapters.holders

import android.bluetooth.BluetoothGattService
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.oncelabs.blehero.databinding.CharacteristicListItemBinding
import com.oncelabs.blehero.databinding.ServiceListItemBinding
import com.oncelabs.blehero.databinding.SettingsDeviceListItemBinding
import com.oncelabs.blehero.model.AppSettingsManager
import com.oncelabs.blehero.model.SavedDevice
import com.oncelabs.blehero.ui.adapters.GattCharacteristicsAdapter
import com.oncelabs.blehero.ui.adapters.GattServicesAdapter
import com.oncelabs.blehero.ui.adapters.SettingsDeviceListAdapter
import com.oncelabs.onceble.core.peripheral.OBPeripheral
import com.oncelabs.onceble.core.peripheral.gattClient.OBCharacteristic
import com.oncelabs.onceble.core.peripheral.gattClient.OBGatt
import com.oncelabs.onceble.core.peripheral.gattClient.OBService

enum class SavedDeviceType{
    FAVORITE,
    IGNORED
}

class SettingsDeviceViewHolder(val binding: SettingsDeviceListItemBinding) : RecyclerView.ViewHolder(binding.root){

    private lateinit var savedDevice: SavedDevice

    fun bind(savedDevice: SavedDevice, savedDeviceType: SavedDeviceType){
        this.savedDevice = savedDevice
        binding.savedDevice = savedDevice

        binding.removeButton.setOnClickListener{
            println("Remove saved device clicked")
            if(savedDeviceType == SavedDeviceType.FAVORITE){
                AppSettingsManager.removeFavoriteDevice(savedDevice.id ?: "")
            } else if(savedDeviceType == SavedDeviceType.IGNORED){
                AppSettingsManager.removeIgnoredDevice(savedDevice.id ?: "")
            }

        }
    }
}