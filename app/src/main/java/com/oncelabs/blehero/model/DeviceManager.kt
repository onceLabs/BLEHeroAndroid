package com.oncelabs.blehero.model

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.oncelabs.onceble.core.central.OBCentralManager
import com.oncelabs.onceble.core.central.OBEvent
import com.oncelabs.onceble.core.peripheral.OBPeripheral
import com.oncelabs.onceble.core.peripheral.gattClient.OBGatt
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

object DeviceManager{

    private var obCentralManager: OBCentralManager? = null

    private val _discoveredDevices = MutableLiveData<List<OBPeripheral<out OBGatt>>>()
    val discoveredDevices : LiveData<List<OBPeripheral<out OBGatt>>>
        get() = _discoveredDevices

    private var discoveredDevicesMap: MutableMap<String, OBPeripheral<out OBGatt>> = mutableMapOf()

    var selectedDeviceForGatt: OBPeripheral<out OBGatt>? = null

    fun init(context: Context){
        obCentralManager = OBCentralManager(loggingEnabled = false, context = context)

        obCentralManager?.bleIsEnabled()?.let{
            if(it){
                startScanning()
            }
            else{
                obCentralManager?.on(OBEvent.BleReady{
                    startScanning()
                })
            }
        }

        obCentralManager?.on(OBEvent.DiscoveredPeripheral{ _obPeripheral, _obAdvertisementData ->
            println("${_obAdvertisementData.name} ${_obAdvertisementData.address}")
            _obPeripheral.id?.let{
                discoveredDevicesMap[it] = _obPeripheral
                updateDiscoveredDevices()
            }
        })
    }

    private fun updateDiscoveredDevices(){

        val devices: MutableList<OBPeripheral<out OBGatt>> = mutableListOf()

        discoveredDevicesMap.forEach{
            devices.add(it.value)
        }

        GlobalScope.launch(Dispatchers.Main) {
            _discoveredDevices.postValue(devices)
        }
    }

    private fun startScanning(){
        //val obScanOptions = OBScanOptions(services = arrayOf(ParcelUuid(LOCK_UUID_CONTROL_SERVICE)))
        obCentralManager?.startScanning()
    }

    private fun stopScanning(){
        obCentralManager?.stopScanning()
    }
}