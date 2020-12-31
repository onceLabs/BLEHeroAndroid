package com.oncelabs.blehero.model

class DeviceDiscoveryFilter {
    var minimumRssi: Int = -127
    var hideNonConnectableDevices: Boolean = false
    var hideUnnamedDevices: Boolean = false
    var onlyShowFavorites: Boolean = false
    var sortByRssi: Boolean = false
    var sortByMostActive: Boolean = false
    var dontSort: Boolean = true

    var sortBySearchString: Boolean = true
    var searchString: String = ""
}