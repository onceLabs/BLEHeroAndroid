package com.oncelabs.blehero.ui

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.oncelabs.blehero.databinding.ActivityCharacteristicViewBinding
import com.oncelabs.blehero.model.DataParser
import com.oncelabs.blehero.model.DeviceManager
import com.oncelabs.onceble.core.peripheral.gattClient.OBCharacteristic


class CharacteristicViewActivity : AppCompatActivity() {

    lateinit var binding: ActivityCharacteristicViewBinding
    private var obCharacteristic: OBCharacteristic? = null

    private lateinit var lifecycleOwner: LifecycleOwner

    private var indicationsEnabled = false

    private var readDataFormat: DataType? = null
    private var writeDataFormat: DataType? = null

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
            binding.readField.setText(
                when(readDataFormat){
                    DataType.HEX -> {DataParser.byteArrayToHexString(it)}
                    DataType.ASCII -> {DataParser.byteArrayToHexString(it)}
                    DataType.DECIMAL -> {DataParser.byteArrayToHexString(it)}
                    null->{""}
                }
            )
        })
    }

    private fun readData(){
        obCharacteristic?.asyncRead {
            binding.readField.setText(it.toString())
        }
    }

    private fun writeData(){
        var byteArray: ByteArray = byteArrayOf()

        when(writeDataFormat){
            DataType.HEX -> {
                byteArray = DataParser.hexStringToByteArray(binding.writeField.text.toString())
            }
            DataType.ASCII -> {
                byteArray = DataParser.asciiStringToByteArray(binding.writeField.text.toString())
            }
            DataType.DECIMAL -> {
//                byteArray = DataParser.hexStringToByteArray(binding.writeField.text.toString())
            }
        }
        obCharacteristic?.asyncWrite(byteArray, withResponse = true){

        }
    }

    private fun setReadChipChoice(dataType: DataType){
        readDataFormat = dataType

        binding.chipHex.isChecked = false
        binding.chipAscii.isChecked = false
        binding.chipDecimal.isChecked = false

        when(dataType){
            DataType.HEX -> {
                binding.chipHex.isChecked = true
            }
            DataType.ASCII -> {
                binding.chipAscii.isChecked = true
            }
            DataType.DECIMAL -> {
                binding.chipDecimal.isChecked = true
            }
        }
    }

    private fun setWriteChipChoice(dataType: DataType){
        writeDataFormat = dataType

        binding.writeChipHex.isChecked = false
        binding.writeChipAscii.isChecked = false
        binding.writeChipDecimal.isChecked = false

        when(dataType){
            DataType.HEX -> {
                binding.writeChipHex.isChecked = true
            }
            DataType.ASCII -> {
                binding.writeChipAscii.isChecked = true
            }
            DataType.DECIMAL -> {
                binding.writeChipDecimal.isChecked = true
            }
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