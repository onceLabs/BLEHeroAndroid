package com.oncelabs.blehero.interfaces

import com.oncelabs.onceble.core.peripheral.gattClient.OBCharacteristic

interface GattInterface {
    fun onCharacteristicSelected(characteristic: OBCharacteristic)
}