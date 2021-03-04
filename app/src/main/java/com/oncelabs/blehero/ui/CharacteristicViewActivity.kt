package com.oncelabs.blehero.ui

import android.annotation.SuppressLint
import android.bluetooth.BluetoothGattService
import android.bluetooth.BluetoothHeadset
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.res.ColorStateList
import android.graphics.Color
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.oncelabs.blehero.R
import com.oncelabs.blehero.databinding.ActivityCharacteristicViewBinding
import com.oncelabs.blehero.databinding.ActivityGattProfileBinding
import com.oncelabs.blehero.databinding.ActivityHelpBinding
import com.oncelabs.blehero.model.DeviceManager
import com.oncelabs.blehero.ui.adapters.DiscoverAdapter
import com.oncelabs.blehero.ui.adapters.GattServicesAdapter
import com.oncelabs.onceble.core.peripheral.gattClient.OBCharacteristic
import com.oncelabs.onceble.core.peripheral.gattClient.OBService
import kotlinx.android.synthetic.main.activity_main.*


class CharacteristicViewActivity : AppCompatActivity() {

    lateinit var binding: ActivityCharacteristicViewBinding
    private var obCharacteristic: OBCharacteristic? = null

    private lateinit var lifecycleOwner: LifecycleOwner

    private var indicationsEnabled = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCharacteristicViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        lifecycleOwner = binding.root.context as LifecycleOwner

        obCharacteristic = DeviceManager.selectedCharacteristic

        initView()

//        window.statusBarColor = ContextCompat.getColor(this, R.color.colorPrimary)

        //Lock screen rotation to portrait
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        bindListeners()
        setReadChipChoice(DataType.HEX)
        setWriteChipChoice(DataType.HEX)
    }


    @SuppressLint("SetTextI18n")
    private fun initView(){
        binding.characteristicUuidLabel.text = "UUID: ${obCharacteristic?.uuid}"

        var propertiesString = ""
        obCharacteristic?.descriptors?.forEach{

        }

        binding.characteristicPropertiesLabel.text = "Properties: $propertiesString"
    }

    private fun bindListeners(){
        binding.chipHex.setOnClickListener {
            setReadChipChoice(DataType.HEX)
        }

        binding.chipAscii.setOnClickListener {
            setReadChipChoice(DataType.ASCII)
        }

        binding.chipDecimal.setOnClickListener {
            setReadChipChoice(DataType.DECIMAL)
        }

        //Write chips
        binding.writeChipHex.setOnClickListener {
            setWriteChipChoice(DataType.HEX)
        }

        binding.writeChipAscii.setOnClickListener {
            setWriteChipChoice(DataType.ASCII)
        }

        binding.writeChipDecimal.setOnClickListener {
            setWriteChipChoice(DataType.DECIMAL)
        }

        binding.readButton.setOnClickListener{
            readData()
        }

        binding.writeButton.setOnClickListener {
            writeData()
        }

        binding.notificationsButton.setOnClickListener {
            indicationsEnabled = !indicationsEnabled
            obCharacteristic?.setIndicationState(indicationsEnabled)

            if(indicationsEnabled){
                binding.notificationsButton.text = "Disable Notifications"
            }else{
                binding.notificationsButton.text = "Enable Notifications"
            }

//            obCharacteristic?.setNotificationState(true)
        }

        obCharacteristic?.liveValue?.observable?.observe(lifecycleOwner, Observer {
            binding.readField.setText(byteArrayToString(it))
        })
    }

    private fun readData(){
        obCharacteristic?.asyncRead {
            binding.readField.setText(it.toString())
        }
    }

    private fun writeData(){
//        obCharacteristic?.asyncWrite()
    }

    private fun setReadChipChoice(dataType: DataType){
        binding.chipHex.isChecked = false
        binding.chipAscii.isChecked = false
        binding.chipDecimal.isChecked = false

        when(dataType){
            DataType.HEX->{
                binding.chipHex.isChecked = true
            }
            DataType.ASCII->{
                binding.chipAscii.isChecked = true
            }
            DataType.DECIMAL->{
                binding.chipDecimal.isChecked = true
            }
        }
    }

    private fun setWriteChipChoice(dataType: DataType){
        binding.writeChipHex.isChecked = false
        binding.writeChipAscii.isChecked = false
        binding.writeChipDecimal.isChecked = false

        when(dataType){
            DataType.HEX->{
                binding.writeChipHex.isChecked = true
            }
            DataType.ASCII->{
                binding.writeChipAscii.isChecked = true
            }
            DataType.DECIMAL->{
                binding.writeChipDecimal.isChecked = true
            }
        }
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
            string
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}

private enum class DataType{
    HEX,
    ASCII,
    DECIMAL
}