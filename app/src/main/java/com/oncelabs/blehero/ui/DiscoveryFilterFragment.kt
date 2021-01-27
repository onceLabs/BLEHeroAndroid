package com.oncelabs.blehero.ui

import android.app.Dialog
import android.content.DialogInterface
import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.FrameLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.oncelabs.blehero.R
import com.oncelabs.blehero.databinding.FragmentDiscoveryFilterBinding


class DiscoveryFilterFragment : BottomSheetDialogFragment(){
//    private val logViewModel: LogViewModel by lazy {
//        ViewModelProvider(this).get(LogViewModel::class.java)
//    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        val binding = FragmentDiscoveryFilterBinding.bind(inflater.inflate(R.layout.fragment_discovery_filter, container, false))
        return binding.root
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