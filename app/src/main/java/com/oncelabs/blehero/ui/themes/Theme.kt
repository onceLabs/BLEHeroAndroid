package com.oncelabs.blehero.ui.themes

import android.graphics.Color
import androidx.databinding.BaseObservable

class Theme: BaseObservable() {
    //Theme object all colors are defined here

    //@get:Bindable
    inner class color{
        //val appBackground = 0xFF1C1C1E
        //val appBackground = Color(0x1C, 0x1C, 0x1C)
        val appForeground = "#2C2C2E"
    }

    //@get:Bindable
    var navBarColor = Color.GRAY


}