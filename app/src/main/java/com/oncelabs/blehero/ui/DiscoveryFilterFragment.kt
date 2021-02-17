package com.oncelabs.blehero.ui

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.oncelabs.blehero.R
import com.oncelabs.blehero.databinding.FragmentDiscoveryFilterBinding
import com.oncelabs.blehero.model.DiscoverFilter
import com.oncelabs.blehero.model.SortSetting
import com.oncelabs.blehero.ui.viewmodels.DiscoverViewModel


class DiscoveryFilterFragment : BottomSheetDialogFragment(){

    private val discoverViewModel: DiscoverViewModel by activityViewModels()
    private lateinit var binding: FragmentDiscoveryFilterBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        binding = FragmentDiscoveryFilterBinding.bind(
            inflater.inflate(
                R.layout.fragment_discovery_filter,
                container,
                false
            )
        )

        binding.filterSettings = DiscoverFilter

        setupBindings()

        return binding.root
    }

    private fun setupBindings(){
        binding.rssiSeekBar.setOnSeekBarChangeListener(object :
            OnSeekBarChangeListener {
            override fun onStopTrackingTouch(seekBar: SeekBar) {
                // TODO Auto-generated method stub
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                // TODO Auto-generated method stub
            }

            @SuppressLint("SetTextI18n")
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                // TODO Auto-generated method stub
                binding.filterRssiLabel.text = "Minimum RSSI: ${progress-127}"
            }
        })


        binding.doneButton.setOnClickListener {
            DiscoverFilter.minimumRssi = binding.rssiSeekBar.progress - 127
            DiscoverFilter.hideNonConnectableDevices = binding.hideNonconnectableDevicesSwitch.isChecked
            DiscoverFilter.hideUnNamedDevices = binding.hideUnnamedDevicesSwitch.isChecked
            DiscoverFilter.onlyShowFavorites = binding.onlyShowFavoritesSwitch.isChecked

            //Sort setting
            var sortSetting = SortSetting.DONT_SORT

            when(binding.sortRadioButtonGroup.checkedRadioButtonId){
                binding.sortByRssiRadioButton.id -> {
                    sortSetting = SortSetting.RSSI
                }
                binding.sortByMostActiveRadioButton.id -> {
                    sortSetting = SortSetting.MOST_ACTIVE
                }
                binding.dontSortRadioButton.id -> {
                    sortSetting = SortSetting.DONT_SORT
                }
            }
            DiscoverFilter.sortSetting = sortSetting

            dismiss()
        }

        binding.resetFilterButton.setOnClickListener{
            //Reset defaults
            DiscoverFilter.reset()

            //Update the UI
            binding.filterSettings = DiscoverFilter
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = BottomSheetDialog(requireContext(), theme)
        dialog.setOnShowListener {

            val bottomSheetDialog = it as BottomSheetDialog
            val parentLayout =
                bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            parentLayout?.let { it ->
                val behaviour = BottomSheetBehavior.from(it)
                setupFullHeight(it)
                behaviour.state = BottomSheetBehavior.STATE_EXPANDED

                behaviour.skipCollapsed = true
            }
        }
        return dialog
    }

    private fun setupFullHeight(bottomSheet: View) {
        val layoutParams = bottomSheet.layoutParams
//        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT
        layoutParams.height = (Resources.getSystem().displayMetrics.heightPixels * 0.9).toInt()
        bottomSheet.layoutParams = layoutParams
    }
}