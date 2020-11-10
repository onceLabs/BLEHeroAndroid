package com.oncelabs.blehero

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.oncelabs.blehero.ui.themes.SystemTheme
import com.oncelabs.blehero.ui.themes.Theme

val systemTheme = MutableLiveData<Theme>()

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        systemTheme.value = SystemTheme.Dark
    }
}