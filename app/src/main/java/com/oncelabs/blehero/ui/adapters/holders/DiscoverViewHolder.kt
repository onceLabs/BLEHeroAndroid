package com.oncelabs.blehero.ui.adapters.holders

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Resources
import android.graphics.Color
import android.util.TypedValue
import android.view.View
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.oncelabs.blehero.databinding.ListDiscoveredDeviceBinding
import com.oncelabs.blehero.model.AppSettingsManager
import com.oncelabs.blehero.model.DeviceManager
import com.oncelabs.blehero.model.DiscoverFilter
import com.oncelabs.blehero.model.SavedDevice
import com.oncelabs.blehero.ui.GattProfileActivity
import com.oncelabs.blehero.ui.viewmodels.DiscoverViewModel
import com.oncelabs.onceble.core.peripheral.ConnectionState
import com.oncelabs.onceble.core.peripheral.OBAdvertisementData
import com.oncelabs.onceble.core.peripheral.OBPeripheral
import com.oncelabs.onceble.core.peripheral.gattClient.OBGatt
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext


class DiscoverViewHolder(val binding: ListDiscoveredDeviceBinding) : RecyclerView.ViewHolder(binding.root){
    private lateinit var graph: LineChart
    private lateinit var _obPeripheral: OBPeripheral<out OBGatt>

    private val graphDataPoints = 15

    fun dispose(){
        binding.peripheral = null
        _obPeripheral.latestAdvData.removeObservers(binding.root.context as LifecycleOwner)
        _obPeripheral.rssiHistorical.removeObservers(binding.root.context as LifecycleOwner)
    }

    fun bind(obPeripheral: OBPeripheral<out OBGatt>){
        _obPeripheral = obPeripheral
        binding.peripheral = _obPeripheral

        //This value is static and set based on view size
        val actionsWidth = 210f
        val r: Resources = Resources.getSystem()
        val translationDistance = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            actionsWidth,
            r.displayMetrics
        )

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

//        binding.gattButton.visibility = View.VISIBLE

        initializeRssiGraph()
        initializeBindings()

        bindActionButtons()
    }

    fun initializeBindings(){
        _obPeripheral.latestAdvData.observe(
            binding.root.context as LifecycleOwner,
            Observer { _advData ->
                updateUI(_advData)
            })

        _obPeripheral.rssiHistorical.observe(binding.root.context as LifecycleOwner, Observer {
            binding.rssiLabel.text = "RSSI: ${it.first()}"

            val list = if (it.size >= graphDataPoints) it.subList(
                it.size - graphDataPoints,
                it.size
            ) else it
            updateRssiGraph(list)
//            println(it.last())
        })
    }

    @SuppressLint("SetTextI18n")
    private fun updateUI(obAdvertisementData: OBAdvertisementData){
        binding.deviceName.text = obAdvertisementData.name.toString()
        binding.macAddressLabel.text = obAdvertisementData.address.toString()
        binding.connectableLabel.text = "Connectable: ${if (obAdvertisementData.connectable == true) "Yes" else "No"}"

        //MFG Data
        var mfgData = byteArrayToString(obAdvertisementData.manufacturerData)
        if(mfgData.isBlank()){
            mfgData = "Not Advertised"
        }
        binding.manufacturerLabel.text = "Manufacturer Data: $mfgData"

//        binding.txPowerLabel.text = "TX Power: ${_obPeripheral.rssiHistorical.value?.first() ?: "Unknown"}"
        binding.servicesLabel.text = "Services: ${obAdvertisementData.serviceUuids ?: "Not Advertised"}"
        binding.serviceDataLabel.text = "Service Data: " +
                "${if(!obAdvertisementData.serviceData.isNullOrEmpty()) obAdvertisementData.serviceData 
                else "Not Advertised"}"

        binding.intervalLabel.text = "Interval (est): ${obAdvertisementData.advInterval} ms"
    }

    private fun bindActionButtons(){
        binding.connectButton.setOnClickListener{
            if(_obPeripheral.isOkToConnect()){
                connectPeripheral()
            }
            else{
                disconnectPeripheral()
            }
        }

        binding.favoriteButton.setOnClickListener{
            _obPeripheral.latestAdvData.value?.let{
                if(AppSettingsManager.isDeviceFavorite(it.address)){
                    AppSettingsManager.removeFavoriteDevice(it.address)
                }
                else{
                    AppSettingsManager.addFavoriteDevice(it.address, it.name)
                }

                updateActionButtonsUi()
            }
        }

        binding.filterButton.setOnClickListener{
            _obPeripheral.latestAdvData.value?.address?.let{
                if(DiscoverFilter.filterAllBut.equals(it, ignoreCase = true)){
                    DiscoverFilter.filterAllBut = ""
                }
                else{
                    DiscoverFilter.filterAllBut = it
                }
                updateActionButtonsUi()
            }
        }

        binding.ignoreButton.setOnClickListener{
            _obPeripheral.latestAdvData.value?.let{
                AppSettingsManager.addIgnoredDevice(it.address, it.name)
            }
        }

        binding.locatorButton.setOnClickListener{

        }

        binding.gattButton.setOnClickListener{
            DeviceManager.selectedDeviceForGatt = _obPeripheral
            showGattActivity()
        }

        updateActionButtonsUi()
    }

    private fun updateActionButtonsUi(){
        _obPeripheral.latestAdvData.value?.let{
            val isFavorite = AppSettingsManager.isDeviceFavorite(it.address)
            binding.favoriteButton.text = if(isFavorite) "Remove Favorite" else "Add Favorite"

            val isFilteringOthers = DiscoverFilter.filterAllBut.equals(it.address, ignoreCase = true)
            binding.filterButton.text = if(isFilteringOthers) "Show all others" else "Filter all others"
        }
    }

    private fun connectPeripheral(){
        _obPeripheral.connect {
            when (it){
                ConnectionState.connecting -> {
                    println("ConnectionState.connecting")
                }
                ConnectionState.connected -> {
                    println("ConnectionState.connected")
                    binding.connectButton.text = "Disconnect"
                }
                ConnectionState.performingGattDiscovery -> {
                    println("ConnectionState.performingGattDiscovery")
                }
                ConnectionState.completedGattDiscovery -> {
                    println("ConnectionState.completedGattDiscovery")
                    GlobalScope.launch(Dispatchers.Main){
                        binding.gattButton.visibility = View.VISIBLE
                    }
                }
                ConnectionState.disconnecting -> {
                    println("ConnectionState.disconnecting")
                }
                ConnectionState.connectionFailed -> {
                    println("ConnectionState.connectionFailed")
                }
                ConnectionState.disconnected -> {
                    println("ConnectionState.disconnected")
                    GlobalScope.launch(Dispatchers.Main){
                        binding.connectButton.text = "Connect"
                        binding.gattButton.visibility = View.GONE
                    }
                }
            }
        }
    }

    private fun disconnectPeripheral(){
        _obPeripheral.disconnect()
        binding.connectButton.text = "Connect"
        binding.gattButton.visibility = View.GONE
    }

    private fun initializeRssiGraph(){
        graph = binding.graphView
        graph.setTouchEnabled(false)
        graph.isScaleYEnabled = false
        graph.legend.isEnabled = false
        graph.description.isEnabled = false
        graph.xAxis.position = XAxis.XAxisPosition.BOTTOM
        graph.xAxis.setDrawLabels(false)
        graph.xAxis.axisLineColor = Color.TRANSPARENT
        graph.axisRight.isEnabled = false
        graph.axisLeft.axisLineColor = Color.TRANSPARENT
        graph.xAxis.granularity = 1f

        graph.axisLeft.axisMaximum = -25f
        graph.axisLeft.axisMinimum = -100f
        graph.xAxis.axisMaximum = (graphDataPoints-1).toFloat()
        graph.xAxis.axisMinimum = 0f
    }

    var rssiData: LineDataSet? = null

    private fun updateRssiGraph(rssiValues: List<Int>){

        val graphValues: MutableList<Entry> = mutableListOf()

        rssiValues.indices.forEach{
            graphValues.add(it, Entry(it.toFloat(), rssiValues[it].toFloat()))
        }

        if(rssiData == null){
            rssiData = LineDataSet(graphValues, "RSSI")

            rssiData?.lineWidth = 2.5f
            rssiData?.setDrawIcons(false)
            rssiData?.setDrawCircles(false)
            rssiData?.setDrawValues(false)
            rssiData?.isHighlightEnabled = false
            rssiData?.mode = LineDataSet.Mode.CUBIC_BEZIER
        }
        else{
            rssiData?.values = graphValues
        }

        rssiData?.let{
            val dataSets: ArrayList<ILineDataSet> = ArrayList()
            dataSets.add(it)
            val data = LineData(dataSets)
            graph.data = data

            graph.invalidate()

            graph.data.notifyDataChanged()
            graph.notifyDataSetChanged()
        }
    }

    private fun showGattActivity(){
        val intent = Intent(binding.root.context, GattProfileActivity::class.java)
        startActivity(binding.root.context, intent, null)
    }

    private fun byteArrayToString(byteArray: ByteArray): String{
        var string = ""

        byteArray?.forEach {
            val byteString = String.format("%02X", it)
            string = "$string$byteString"
        }

        return if(string.isBlank()){
            ""
        }else {
            "0x$string"
        }
    }
}