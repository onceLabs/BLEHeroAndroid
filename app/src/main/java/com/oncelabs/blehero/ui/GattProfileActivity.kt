package com.oncelabs.blehero.ui

import android.bluetooth.BluetoothGattService
import android.bluetooth.BluetoothHeadset
import android.content.pm.ActivityInfo
import android.content.res.ColorStateList
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.oncelabs.blehero.R
import com.oncelabs.blehero.databinding.ActivityGattProfileBinding
import com.oncelabs.blehero.model.DeviceManager
import com.oncelabs.blehero.ui.adapters.DiscoverAdapter
import com.oncelabs.blehero.ui.adapters.GattServicesAdapter
import com.oncelabs.onceble.core.peripheral.gattClient.OBService
import kotlinx.android.synthetic.main.activity_main.*


class GattProfileActivity : AppCompatActivity() {

    lateinit var binding: ActivityGattProfileBinding
    private var adapter : GattServicesAdapter? = null

    private var _obPeripheral = DeviceManager.selectedDeviceForGatt

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGattProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        window.statusBarColor = ContextCompat.getColor(this, R.color.colorPrimary)

        //Lock screen rotation to portrait
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        binding.deviceName.text = _obPeripheral?.latestAdvData?.value?.name ?: "null"

        initList()
        bindListeners()
    }

    private fun bindListeners(){
        binding.backButton.setOnClickListener {
            finish()
        }
    }

    private fun initList() {
        if(adapter == null) {
            adapter = GattServicesAdapter()
            binding.servicesList.layoutManager = LinearLayoutManager(this)
            binding.servicesList.adapter = adapter

            val tempList = mutableListOf<OBService>()
            _obPeripheral?.obGatt?.services?.forEach {
                tempList.add(it.value)
                println("Added service = ${it.key}")
            }

            adapter?.updateServices(tempList)
        } else {
            //adapter?.updateCallback(this)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        DeviceManager.selectedDeviceForGatt = null
    }
}