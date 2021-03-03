package com.oncelabs.blehero.ui

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.oncelabs.blehero.R
import com.oncelabs.blehero.databinding.ManageFavoritesDialogFragmentBinding
import com.oncelabs.blehero.model.AppSettings
import com.oncelabs.blehero.model.AppSettingsManager
import com.oncelabs.blehero.model.DiscoverFilter
import com.oncelabs.blehero.model.SavedDevice
import com.oncelabs.blehero.ui.adapters.GattCharacteristicsAdapter
import com.oncelabs.blehero.ui.adapters.SettingsDeviceListAdapter
import com.oncelabs.blehero.ui.adapters.holders.SavedDeviceType
import com.oncelabs.onceble.core.peripheral.gattClient.OBCharacteristic
import kotlinx.android.synthetic.main.manage_favorites_dialog_fragment.*

class ManageFavoritesDialogFragment : DialogFragment() {

    private var adapter : SettingsDeviceListAdapter? = null
    private lateinit var binding: ManageFavoritesDialogFragmentBinding
    private var savedDeviceType: SavedDeviceType? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog{
        binding = ManageFavoritesDialogFragmentBinding
            .inflate(LayoutInflater.from(context));

        val dialog = AlertDialog.Builder(requireContext())
            .setView(binding.root)
            .create()

        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        arguments?.getInt("saved_device_type")?.let {
            savedDeviceType = SavedDeviceType.values()[it]
        }

        if(savedDeviceType == null){
            dismiss()
        }


        return dialog
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        initList()

        when(savedDeviceType){
            SavedDeviceType.FAVORITE ->{
                binding.pageTitle.text = "Favorites"
                binding.deviceExplanationLabel.text = "The devices that were added to favorites on the \"Discover\" page"
            }
            SavedDeviceType.IGNORED->{
                binding.pageTitle.text = "Ignored"
                binding.deviceExplanationLabel.text = "The devices that were ignored on the \"Discover\" page"
            }
        }

            return binding.root
    }

    companion object {
        const val TAG = "ManagerFavoritesDialog"
    }

    private fun initList() {
        if(adapter == null) {
            adapter = SettingsDeviceListAdapter()
            binding.savedDevicesList.layoutManager = LinearLayoutManager(context)
            binding.savedDevicesList.adapter = adapter


            AppSettingsManager.appSettings.observe(viewLifecycleOwner, Observer { appSettings ->
                val devices: List<SavedDevice> =
                when(savedDeviceType){
                    SavedDeviceType.FAVORITE ->{
                        appSettings.favoriteDevices
                    }
                    SavedDeviceType.IGNORED->{
                        appSettings.ignoredDevices
                    }
                    null->{
                        listOf()
                    }
                }

                adapter?.updateDevices(devices, savedDeviceType!!)

                if(devices.isEmpty()){
                    dismiss()
                }
            })

        }
    }
}