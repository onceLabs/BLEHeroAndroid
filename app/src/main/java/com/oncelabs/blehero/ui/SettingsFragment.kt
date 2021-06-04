package com.oncelabs.blehero.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.oncelabs.blehero.R
import com.oncelabs.blehero.databinding.FragmentSettingsBinding
import com.oncelabs.blehero.model.AppSettingsManager
import com.oncelabs.blehero.ui.adapters.holders.SavedDeviceType
import com.oncelabs.blehero.ui.viewmodels.SettingsViewModel
import java.net.URL


class SettingsFragment : Fragment() {

    private val settingsViewModel: SettingsViewModel by lazy {
        ViewModelProvider(this).get(SettingsViewModel::class.java)
    }

    lateinit var binding: FragmentSettingsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        binding = FragmentSettingsBinding.bind(
            inflater.inflate(
                R.layout.fragment_settings,
                container,
                false
            )
        )
        binding.viewModel = settingsViewModel

        setBindings()

        bindListeners()

//        updateUi()
        return binding.root
    }

    private fun bindListeners(){
        AppSettingsManager.appSettings.observe(viewLifecycleOwner, Observer {
            updateUi()
        })
    }

    @SuppressLint("SetTextI18n")
    fun updateUi(){

        val appSettings = AppSettingsManager.appSettings.value ?: return

        val dataTimeoutSeconds: Int = appSettings.dataTimeout ?: 60
        val dataTimeoutMinutes: Int = dataTimeoutSeconds/60
        val isMinutes = dataTimeoutSeconds >= 60
        binding.dataTimeoutLabel.text = if(isMinutes) "$dataTimeoutMinutes " +
                if (dataTimeoutMinutes > 1) "minutes" else "minute" else "$dataTimeoutSeconds seconds"

        binding.favoritedDevicesLabel.text = "${appSettings.favoriteDevices.size} ${if (appSettings.favoriteDevices.size == 1) "device" else "devices"}"
        binding.ignoredDevicesLabel.text = "${appSettings.ignoredDevices.size} ${if (appSettings.ignoredDevices.size == 1) "device" else "devices"}"

        binding.advertisementDataSwitch.isChecked = appSettings.logAdvertisementData
        binding.connectionEventsSwitch.isChecked = appSettings.logConnectionEvents
        binding.disconnectionEventsSwitch.isChecked = appSettings.logDisconnectionEvents
        binding.readWriteEventsSwitch.isChecked = appSettings.logReadWriteEvents
        binding.notificationIndicationEventsSwitch.isChecked = appSettings.logNotificationIndicationEvents
        binding.gattDiscoveryEventsSwitch.isChecked = appSettings.logGattDiscoveryEvents
        binding.rssiUpdatesSwitch.isChecked = appSettings.logRssiUpdates

        try {
            val pInfo = context?.packageManager?.getPackageInfo(requireContext().packageName, 0)
            val version = pInfo?.versionName ?: ""
            binding.appVersionLabel.text = version
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
    }

    private fun setBindings(){

        binding.helpButton.setOnClickListener {
            startActivity(Intent(context, HelpActivity::class.java))
        }

        binding.privacyPolicyButton.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://www.oncelabs.com/privacy_policy.html")))
        }

        binding.sendEmailButton.setOnClickListener {
            val email = "support@oncelabs.com?subject=BLE%20Hero"
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("mailto:${email}")))
        }

        binding.reportIssueButton.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/onceLabs/iOS-BLE-Hero/issues/new")))
        }


        binding.favoritesButton.setOnClickListener {
            if(AppSettingsManager.appSettings.value?.favoriteDevices?.isNotEmpty() == true){
                val fragment = ManageFavoritesDialogFragment()
                val args = Bundle()
                args.putInt("saved_device_type", SavedDeviceType.FAVORITE.ordinal)
                fragment.arguments = args
                fragment.show(childFragmentManager, ManageFavoritesDialogFragment.TAG)
            }
        }

        binding.ignoredDevicesButton.setOnClickListener {
            if(AppSettingsManager.appSettings.value?.ignoredDevices?.isNotEmpty() == true) {
                val fragment = ManageFavoritesDialogFragment()
                val args = Bundle()
                args.putInt("saved_device_type", SavedDeviceType.IGNORED.ordinal)
                fragment.arguments = args
                fragment.show(childFragmentManager, ManageFavoritesDialogFragment.TAG)
            }
        }

        binding.advertisementDataSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            val appSettings = AppSettingsManager.appSettings.value ?: return@setOnCheckedChangeListener
            appSettings.logAdvertisementData = isChecked
            AppSettingsManager.saveAppSettings(appSettings)
        }

        binding.connectionEventsSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            val appSettings = AppSettingsManager.appSettings.value ?: return@setOnCheckedChangeListener
            appSettings.logConnectionEvents = isChecked
            AppSettingsManager.saveAppSettings(appSettings)
        }

        binding.disconnectionEventsSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            val appSettings = AppSettingsManager.appSettings.value ?: return@setOnCheckedChangeListener
            appSettings.logDisconnectionEvents = isChecked
            AppSettingsManager.saveAppSettings(appSettings)
        }

        binding.readWriteEventsSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            val appSettings = AppSettingsManager.appSettings.value ?: return@setOnCheckedChangeListener
            appSettings.logReadWriteEvents = isChecked
            AppSettingsManager.saveAppSettings(appSettings)
        }

        binding.notificationIndicationEventsSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            val appSettings = AppSettingsManager.appSettings.value ?: return@setOnCheckedChangeListener
            appSettings.logNotificationIndicationEvents = isChecked
            AppSettingsManager.saveAppSettings(appSettings)
        }

        binding.gattDiscoveryEventsSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            val appSettings = AppSettingsManager.appSettings.value ?: return@setOnCheckedChangeListener
            appSettings.logGattDiscoveryEvents = isChecked
            AppSettingsManager.saveAppSettings(appSettings)
        }

        binding.rssiUpdatesSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            val appSettings = AppSettingsManager.appSettings.value ?: return@setOnCheckedChangeListener
            appSettings.logRssiUpdates = isChecked
            AppSettingsManager.saveAppSettings(appSettings)
        }
    }
}