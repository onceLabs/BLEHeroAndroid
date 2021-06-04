package com.oncelabs.blehero.ui.adapters.holders

import android.bluetooth.BluetoothGattService
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.oncelabs.blehero.databinding.CharacteristicListItemBinding
import com.oncelabs.blehero.databinding.ServiceListItemBinding
import com.oncelabs.blehero.model.DeviceManager
import com.oncelabs.blehero.ui.CharacteristicViewActivity
import com.oncelabs.blehero.ui.HelpActivity
import com.oncelabs.blehero.ui.adapters.GattCharacteristicsAdapter
import com.oncelabs.blehero.ui.adapters.GattServicesAdapter
import com.oncelabs.onceble.core.peripheral.OBPeripheral
import com.oncelabs.onceble.core.peripheral.gattClient.OBCharacteristic
import com.oncelabs.onceble.core.peripheral.gattClient.OBGatt
import com.oncelabs.onceble.core.peripheral.gattClient.OBService


class CharacteristicsViewHolder(val binding: CharacteristicListItemBinding) : RecyclerView.ViewHolder(binding.root){

    private var _obCharacteristic: OBCharacteristic? = null
    private var adapter : GattCharacteristicsAdapter? = null

    fun bind(obCharacteristic: OBCharacteristic){
        _obCharacteristic = obCharacteristic
        binding.characteristic = obCharacteristic

        bindListeners()
    }

    private fun bindListeners(){
        binding.root.setOnClickListener {
            _obCharacteristic?.let{
                DeviceManager.selectedCharacteristic = it
            }
            binding.root.context.startActivity(Intent(binding.root.context, CharacteristicViewActivity::class.java))
        }
    }
}