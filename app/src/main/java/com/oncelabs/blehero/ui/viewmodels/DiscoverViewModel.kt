package com.oncelabs.blehero.ui.viewmodels

import androidx.lifecycle.ViewModel


class DiscoverViewModel : ViewModel(){
    val discoverFilterSettings = DiscoverFilterSettings()

}

class DiscoverFilterSettings{
    var minimumRssi: Int = -127
    var hideNonConnectableDevices: Boolean = false
    var hideUnNamedDevices: Boolean = false
    var onlyShowFavorites: Boolean = false

    var sortSetting: SortSetting = SortSetting.DONT_SORT


}

enum class SortSetting{
    RSSI,
    MOST_ACTIVE,
    DONT_SORT
}