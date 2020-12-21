package com.oncelabs.blehero.ui.adapters.holders

import android.content.res.Resources
import android.util.TypedValue
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import com.oncelabs.blehero.R
import com.oncelabs.blehero.databinding.ListDiscoveredDeviceBinding
import com.oncelabs.onceble.core.peripheral.OBAdvertisementData
import com.oncelabs.onceble.core.peripheral.OBPeripheral


class DiscoverViewHolder(val binding: ListDiscoveredDeviceBinding) : RecyclerView.ViewHolder(binding.root){
    fun bind(obPeripheral: OBPeripheral){
        binding.peripheral = obPeripheral

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

        obPeripheral.latestAdvData.observe(binding.root.context as LifecycleOwner, Observer {
            updateUI(it)
        })

        val graph = binding.graphView

        //Setup UI
        val renderer = graph.gridLabelRenderer
        renderer.gridColor = ContextCompat.getColor(binding.root.context, R.color.foregroundLight)
        renderer.isHorizontalLabelsVisible = false
        renderer.verticalLabelsColor = ContextCompat.getColor(binding.root.context, R.color.primaryText)
        renderer.textSize = 24f

        graph.viewport.setMinX(0.toDouble());
        graph.viewport.setMaxX(10.toDouble());
        graph.viewport.setMinY((-100).toDouble());
        graph.viewport.setMaxY(0.toDouble());

        graph.viewport.isYAxisBoundsManual = true
        graph.viewport.isXAxisBoundsManual = true


        val series: LineGraphSeries<DataPoint> = LineGraphSeries(
            arrayOf(
                DataPoint(0.toDouble(), (-100).toDouble()),
                DataPoint(1.toDouble(), (-85).toDouble()),
                DataPoint(2.toDouble(), (-80).toDouble()),
                DataPoint(3.toDouble(), (-70).toDouble()),
                DataPoint(4.toDouble(), (-75).toDouble()),
                DataPoint(5.toDouble(), (-70).toDouble()),
                DataPoint(6.toDouble(), (-65).toDouble()),
                DataPoint(7.toDouble(), (-70).toDouble()),
                DataPoint(8.toDouble(), (-72).toDouble()),
                DataPoint(9.toDouble(), (-73).toDouble()),
                DataPoint(10.toDouble(), (-74).toDouble())
            )
        )

        //series.setAnimated(true)
        series.isDrawBackground = true
        series.backgroundColor = ContextCompat.getColor(binding.root.context, R.color.graphBackgroundTranslucent)
        series.color = ContextCompat.getColor(binding.root.context, R.color.colorAccent)
        graph.addSeries(series)
    }

    private fun updateUI(obAdvertisementData: OBAdvertisementData){
        binding.deviceName.text = obAdvertisementData.name.toString()
        binding.macAddressLabel.text = obAdvertisementData.address.toString()
        binding.connectableLabel.text = "Connectable: ${if (obAdvertisementData.connectable == true) "Yes" else "No"}"
    }

    private fun updateRssiGraph(){

    }
}