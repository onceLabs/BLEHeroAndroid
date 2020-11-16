package com.oncelabs.blehero.ui.themes

import android.graphics.Color
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.library.baseAdapters.BR

class Theme: BaseObservable() {
    //Theme object all colors are defined here

    @get:Bindable
    var color = Color.RED

    @get:Bindable
    var navBarColor = Color.GRAY


}