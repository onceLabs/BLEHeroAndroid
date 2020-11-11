package com.oncelabs.blehero.ui.themes

import android.graphics.Color
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.library.baseAdapters.BR

object GlobalTheme: BaseObservable() {
    //Theme object all colors are defined here

    @get:Bindable
    var theme: Theme = SystemTheme.Dark
        set(value) {
            field = value
            notifyPropertyChanged(BR.theme)
        }
}