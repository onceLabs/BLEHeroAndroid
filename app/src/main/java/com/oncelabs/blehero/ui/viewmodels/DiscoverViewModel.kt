package com.oncelabs.blehero.ui.viewmodels

import android.os.Looper
import androidx.lifecycle.*
import com.oncelabs.blehero.model.DeviceManager
import com.oncelabs.onceble.core.peripheral.OBPeripheral
import com.oncelabs.onceble.core.peripheral.gattClient.OBGatt
import kotlinx.android.synthetic.main.fragment_discover.*


class DiscoverViewModel : ViewModel(){
    var discoverFilter = DiscoverFilter()
    val discoveredDevices = MutableLiveData<List<OBPeripheral<out OBGatt>>>()
    val filteredDevices = MutableLiveData<List<OBPeripheral<out OBGatt>>>()

    val favoritesAddressList = mutableListOf<String>()

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
        startFilterTimer()
    }

    fun dispose(){
        stopFilterTimer()
    }

    private val discoveredDevicesObserver: Observer<List<OBPeripheral<out OBGatt>>> = Observer { obPeripheralList ->
        discoveredDevices.value = obPeripheralList.toMutableList()
        updateDevices()
    }

    private fun getFilteredDevices(deviceList: List<OBPeripheral<out OBGatt>>): List<OBPeripheral<out OBGatt>>{
        var filteredDevices = mutableListOf<OBPeripheral<out OBGatt>>()

        deviceList.forEach{device ->
            var addDevice = true

            if(!((device.latestAdvData.value?.searchableString?.toLowerCase()?.contains(discoverFilter.searchString.toLowerCase()) == true) ||
                discoverFilter.searchString.isBlank())){
                addDevice = false
            }

            if((device.latestAdvData.value?.rssi ?: -127) < discoverFilter.minimumRssi){
                addDevice = false
            }

            if(discoverFilter.hideNonConnectableDevices && (device.latestAdvData.value?.connectable == false)){
                addDevice = false
            }

            if(discoverFilter.hideUnNamedDevices && (device.latestAdvData.value?.name.isNullOrBlank())){
                addDevice = false
            }

            if(discoverFilter.onlyShowFavorites && !favoritesAddressList.contains(device.latestAdvData.value?.address)){
                addDevice = false
            }


            if(addDevice){
                filteredDevices.add(device)
            }
        }

        //Sort filtered devices
        when(discoverFilter.sortSetting){
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
        println("Updateing devices")
        discoveredDevices.value?.let{
            val tempFilteredDevices = getFilteredDevices(it)

            if(tempFilteredDevices != filteredDevices.value){
                filteredDevices.value = getFilteredDevices(it)
            }
        }
    }
}

class DiscoverFilter{
    var minimumRssi: Int = -127
    var hideNonConnectableDevices: Boolean = false
    var hideUnNamedDevices: Boolean = false
    var onlyShowFavorites: Boolean = false
    var sortSetting: SortSetting = SortSetting.DONT_SORT
    var searchString: String = ""
}

enum class SortSetting{
    RSSI,
    MOST_ACTIVE,
    DONT_SORT
}