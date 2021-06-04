package com.oncelabs.blehero.model

//class DeviceDiscoveryFilter {
//    var minimumRssi: Int = -127
//    var hideNonConnectableDevices: Boolean = false
//    var hideUnnamedDevices: Boolean = false
//    var onlyShowFavorites: Boolean = false
//    var sortByRssi: Boolean = false
//    var sortByMostActive: Boolean = false
//    var dontSort: Boolean = true
//
//    var sortBySearchString: Boolean = true
//    var searchString: String = ""
//}

object DiscoverFilter{
    var minimumRssi: Int = -127
    var hideNonConnectableDevices: Boolean = false
    var hideUnNamedDevices: Boolean = false
    var onlyShowFavorites: Boolean = false
    var sortSetting: SortSetting = SortSetting.DONT_SORT
    var searchString: String = ""

    var filterAllBut: String = ""

    fun reset(){
        minimumRssi = -127
        hideNonConnectableDevices = false
        hideUnNamedDevices = false
        onlyShowFavorites = false
        sortSetting = SortSetting.DONT_SORT
        searchString = ""
        filterAllBut = ""
    }
}

enum class SortSetting{
    RSSI,
    MOST_ACTIVE,
    DONT_SORT
}