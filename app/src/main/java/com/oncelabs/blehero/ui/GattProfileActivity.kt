package com.oncelabs.blehero.ui

import android.content.pm.ActivityInfo
import android.content.res.ColorStateList
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.oncelabs.blehero.R
import com.oncelabs.blehero.databinding.ActivityGattProfileBinding
import com.oncelabs.blehero.model.DeviceManager
import kotlinx.android.synthetic.main.activity_main.*


class GattProfileActivity : AppCompatActivity() {

    lateinit var binding: ActivityGattProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGattProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        window.statusBarColor = ContextCompat.getColor(this, R.color.colorPrimary)

        //Lock screen rotation to portrait
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        bindListeners()
    }

    private fun bindListeners(){
        binding.backButton.setOnClickListener {
            finish()
        }
    }
}