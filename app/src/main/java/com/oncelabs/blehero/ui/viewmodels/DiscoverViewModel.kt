package com.oncelabs.blehero.ui.viewmodels

import android.os.Looper
import androidx.lifecycle.*
import com.oncelabs.blehero.model.*
import com.oncelabs.onceble.core.peripheral.OBPeripheral
import com.oncelabs.onceble.core.peripheral.gattClient.OBGatt
import kotlinx.android.synthetic.main.fragment_discover.*


class DiscoverViewModel : ViewModel(){
//    var DiscoverFilter = DiscoverFilter()
    val discoveredDevices = MutableLiveData<List<OBPeripheral<out OBGatt>>>()
    val filteredDevices = MutableLiveData<List<OBPeripheral<out OBGatt>>>()

    val favoritesAddressList = mutableListOf<String>()
    val ignoredAddressList = mutableListOf<String>()

    private val mainHandler = android.os.Handler(Looper.getMainLooper())

    private val filterUpdateTimerTask = object : Runnable {
        override fun run() {
            updateDevices()
            mainHandler.postDelayed(this, 1000)
        }
    }

    private fun startFilterTimer(){
        mainHandler.post(filterUpdateTimerTask)
    }

    private fun stopFilterTimer(){
        mainHandler.removeCallbacks(filterUpdateTimerTask)
    }

    fun init(viewLifecycleOwner: LifecycleOwner){
        DeviceManager.discoveredDevices.observe(viewLifecycleOwner, discoveredDevicesObserver)
        AppSettingsManager.appSettings.observe(viewLifecycleOwner, appSettingsObserver)
        startFilterTimer()
    }

    fun dispose(){
        stopFilterTimer()
    }

    private val appSettingsObserver: Observer<AppSettings> = Observer {
        favoritesAddressList.clear()
        ignoredAddressList.clear()

        it.favoriteDevices.forEach { device ->
            device.id?.let { id -> favoritesAddressList.add(id)}
        }

        it.ignoredDevices.forEach { device ->
            device.id?.let { id -> ignoredAddressList.add(id)}
        }

        updateDevices()
    }

    private val discoveredDevicesObserver: Observer<List<OBPeripheral<out OBGatt>>> = Observer { obPeripheralList ->
        discoveredDevices.value = obPeripheralList.toMutableList()
        updateDevices()
    }

    private fun getFilteredDevices(deviceList: List<OBPeripheral<out OBGatt>>): List<OBPeripheral<out OBGatt>>{
        var filteredDevices = mutableListOf<OBPeripheral<out OBGatt>>()

        deviceList.forEach{device ->
            var addDevice = true

            if(!((device.latestAdvData.value?.searchableString?.toLowerCase()?.contains(DiscoverFilter.searchString.toLowerCase()) == true) ||
                DiscoverFilter.searchString.isBlank())){
                addDevice = false
            }

            if((device.latestAdvData.value?.rssi ?: -127) < DiscoverFilter.minimumRssi){
                addDevice = false
            }

            if(DiscoverFilter.hideNonConnectableDevices && (device.latestAdvData.value?.connectable == false)){
                addDevice = false
            }

            if(DiscoverFilter.hideUnNamedDevices && (device.latestAdvData.value?.name.isNullOrBlank())){
                addDevice = false
            }

            if(DiscoverFilter.onlyShowFavorites && !favoritesAddressList.contains(device.latestAdvData.value?.address)){
                addDevice = false
            }

            if(ignoredAddressList.contains(device.latestAdvData.value?.address)){
                addDevice = false
            }

            if((DiscoverFilter.filterAllBut.isNotBlank()) && (DiscoverFilter.filterAllBut != device.latestAdvData.value?.address)){
                addDevice = false
            }


            if(addDevice){
                filteredDevices.add(device)
            }
        }

        //Sort filtered devices
        when(DiscoverFilter.sortSetting){
            SortSetting.DONT_SORT->{
                //Do nothing here
            }
            SortSetting.RSSI->{
                filteredDevices.sortBy {
                    it.latestAdvData.value?.rssi
                }
            }
            SortSetting.MOST_ACTIVE->{
                filteredDevices.sortBy {
                    it.latestAdvData.value?.advInterval
                }
            }
        }

        return filteredDevices.toList()
    }

    fun updateDevices(){
//        println("Updateing devices")
        discoveredDevices.value?.let{
            val tempFilteredDevices = getFilteredDevices(it)

            if(tempFilteredDevices != filteredDevices.value){
                filteredDevices.value = getFilteredDevices(it)
            }
        }
    }
}