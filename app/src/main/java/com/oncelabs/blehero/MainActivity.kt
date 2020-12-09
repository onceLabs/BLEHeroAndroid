package com.oncelabs.blehero

import android.content.pm.ActivityInfo
import android.content.res.ColorStateList
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Lock screen rotation to portrait
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)
        navView.setupWithNavController(navController)
        navView.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary))
        navView.itemTextAppearanceActive = R.style.navBarTextActive
        navView.itemTextAppearanceInactive = R.style.navBarTextInactive
        navView.itemIconSize = 70
        navView.itemIconTintList = ColorStateList.valueOf(Color.WHITE)
        navView.elevation = 3f

        nav_view.setOnNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.navigation_discover-> {
                    navController.navigate(R.id.discoverFragment)
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.navigation_logs -> {
                    navController.navigate(R.id.logFragment)
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.navigation_settings -> {
                    navController.navigate(R.id.settingsFragment)
                    return@setOnNavigationItemSelectedListener true
                }
            }
            false
        }

    }
}