package com.oncelabs.blehero

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import androidx.databinding.DataBindingUtil
import androidx.databinding.DataBindingUtil.setContentView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.oncelabs.blehero.databinding.ActivityMainBinding
import com.oncelabs.blehero.ui.themes.GlobalTheme
import com.oncelabs.blehero.ui.themes.SystemTheme
import com.oncelabs.blehero.ui.themes.Theme
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = setContentView<ActivityMainBinding>(this, R.layout.activity_main)

        binding.globalTheme = GlobalTheme

        button.setOnClickListener {
            println("Ive been pressed")
            if(GlobalTheme.theme == SystemTheme.Light){
                GlobalTheme.theme = SystemTheme.Dark
            }
            else{
                GlobalTheme.theme = SystemTheme.Light
            }

        }
    }
}