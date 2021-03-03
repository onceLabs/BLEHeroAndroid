package com.oncelabs.blehero.ui

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
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.oncelabs.blehero.R
import com.oncelabs.blehero.databinding.ActivityGattProfileBinding
import com.oncelabs.blehero.databinding.ActivityHelpBinding
import com.oncelabs.blehero.model.DeviceManager
import com.oncelabs.blehero.ui.adapters.DiscoverAdapter
import com.oncelabs.blehero.ui.adapters.GattServicesAdapter
import com.oncelabs.onceble.core.peripheral.gattClient.OBService
import kotlinx.android.synthetic.main.activity_main.*


class HelpActivity : AppCompatActivity() {

    lateinit var binding: ActivityHelpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHelpBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        window.statusBarColor = ContextCompat.getColor(this, R.color.colorPrimary)

        //Lock screen rotation to portrait
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        bindListeners()
    }

    private fun bindListeners(){
        //Scanning
        binding.scanningOverview.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://youtu.be/5xCsp4J2RzI")))
        }

        binding.searchFunction.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://youtu.be/cVP1YtuiSzk")))
        }

        binding.filtering.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://youtu.be/f3eneP0vNt0")))
        }

        binding.sorting.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://youtu.be/TRTKCZxagY4")))
        }

        binding.favorites.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://youtu.be/hOsaJ19cTfE")))
        }

        binding.filterAllOthers.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://youtu.be/OBBo8RIGebg")))
        }

        binding.ignoringDevices.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://youtu.be/mkU4qawrjo8")))
        }

        binding.locatorMode.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://youtu.be/oc_CBiUWs54")))
        }

        //GATT
        binding.gattProfile.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://youtu.be/IJQFkQlhLLI")))
        }

        binding.reading.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://youtu.be/PpYqMelJ7RU")))
        }

        binding.writing.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://youtu.be/sJtW2of-yzs")))
        }

        binding.notificationsIndications.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://youtu.be/w10tCJRygRs")))
        }

        binding.deviceFirmwareUpgrade.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://youtu.be/VmG361Uj-GM")))
        }

        //Logging
        binding.loggingOverview.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://youtu.be/2WlrEWmMfjA")))
        }

        //Settings
        binding.settingsOverview.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://youtu.be/U5fw1AsgU-Y")))
        }

        binding.backButton.setOnClickListener {
            onBackPressed()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}