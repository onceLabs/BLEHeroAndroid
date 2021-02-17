package com.oncelabs.blehero.model

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.oncelabs.blehero.R
import org.json.JSONArray
import org.json.JSONObject

object DataKeys{
    val logAdvertisementDataKey = "log_advertisement_data"
    val logConnectionEventsKey = "log_connection_events"
    val logDisconnectionEventsKey = "log_disconnection_events"
    val logReadWriteEventsKey = "log_read_write_events"
    val logNotificationIndicationEventsKey = "log_notification_indication_events"
    val logGattDiscoveryEventsKey = "log_gatt_discovery_events"
    val logRssiUpdatesKey = "log_rssi_updates"
}

object AppSettingsManager{
    var appSettings = MutableLiveData<AppSettings>(AppSettings())
    lateinit var context: Context

    fun init(context: Context) {
        this.context = context
        this.appSettings.value = _getAppSettings()
//        clearAppSettings()
    }

    fun addIgnoredDevice(macAddress: String, name: String?){
        val tempSavedDevice = SavedDevice(macAddress, name ?: "null")
        this.appSettings.value?.ignoredDevices?.let{
            if(!it.contains(tempSavedDevice)){
                it.add(tempSavedDevice)
                this.appSettings.value?.let{settings ->
                    saveAppSettings(settings)
                }
            }
        }
    }

    fun removeIgnoredDevice(macAddress: String){
        this.appSettings.value?.ignoredDevices?.forEach{
            if(it.id?.equals(macAddress, ignoreCase = true) == true){
                this.appSettings.value?.let{settings ->
                    settings.ignoredDevices.remove(it)
                    saveAppSettings(settings)
                }
            }
        }
    }

    fun isDeviceFavorite(macAddress: String) : Boolean{
        var isContained = false

        this.appSettings.value?.favoriteDevices?.forEach{
            if(it.id.equals(macAddress, ignoreCase = true)){
                isContained = true
            }
        }

        return isContained
    }

    fun addFavoriteDevice(macAddress: String, name: String?){
        val tempSavedDevice = SavedDevice(macAddress, name ?: "null")
        this.appSettings.value?.favoriteDevices?.let{
            if(!it.contains(tempSavedDevice)){
                it.add(tempSavedDevice)
                this.appSettings.value?.let{settings ->
                    saveAppSettings(settings)
                }
            }
        }
    }

    fun removeFavoriteDevice(macAddress: String){
        this.appSettings.value?.favoriteDevices?.forEach{
            if(it.id?.equals(macAddress, ignoreCase = true) == true){
                this.appSettings.value?.let{settings ->
                    settings.favoriteDevices.remove(it)
                    saveAppSettings(settings)
                }
            }
        }
    }



    fun clearAppSettings(){
        val sharedPref = (context as AppCompatActivity).getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)
        sharedPref.edit().remove("app_settings").apply()

        this.appSettings.value = AppSettings()
    }

    fun _getAppSettings(): AppSettings{
        val sharedPref = (context as AppCompatActivity).getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)
        val jsonString: String? = sharedPref.getString("app_settings", null)

        if(sharedPref == null || jsonString == null){
            return AppSettings()
        }

        val appSettingsFromJson = AppSettingsFromJson(jsonString)

        println("returning app settings")

        return AppSettings(
            appSettingsFromJson.dataTimeout,
            appSettingsFromJson.favoritedDevices?.toMutableList() ?: mutableListOf(),
            appSettingsFromJson.ignoredDevices?.toMutableList() ?: mutableListOf(),
            appSettingsFromJson.logAdvertisementData,
            appSettingsFromJson.logConnectionEvents,
            appSettingsFromJson.logDisconnectionEvents,
            appSettingsFromJson.logReadWriteEvents,
            appSettingsFromJson.logNotificationIndicationEvents,
            appSettingsFromJson.logGattDiscoveryEvents,
            appSettingsFromJson.logRssiUpdates
        )
    }

    fun saveAppSettings(appSettings: AppSettings){
        val jsonObject = JSONObject()

        jsonObject.putOpt("data_timeout", appSettings.dataTimeout?:60)

        //Logging values
        jsonObject.putOpt(DataKeys.logAdvertisementDataKey, appSettings.logAdvertisementData)
        jsonObject.putOpt(DataKeys.logConnectionEventsKey, appSettings.logConnectionEvents)
        jsonObject.putOpt(DataKeys.logDisconnectionEventsKey, appSettings.logDisconnectionEvents)
        jsonObject.putOpt(DataKeys.logReadWriteEventsKey, appSettings.logReadWriteEvents)
        jsonObject.putOpt(DataKeys.logNotificationIndicationEventsKey, appSettings.logNotificationIndicationEvents)
        jsonObject.putOpt(DataKeys.logGattDiscoveryEventsKey, appSettings.logGattDiscoveryEvents)
        jsonObject.putOpt(DataKeys.logRssiUpdatesKey, appSettings.logRssiUpdates)

        val favoritedDevicesJsonArray = JSONArray()
        val ignoredDevicesJsonArray = JSONArray()

        appSettings.favoriteDevices.forEach {
            val tempJsonObject = JSONObject()

            tempJsonObject.putOpt("id", it.id)
            tempJsonObject.putOpt("name", it.name)
            favoritedDevicesJsonArray.put(tempJsonObject)
        }

        //Have to put a null object at the end or it doesn't work, kotlin's fault
        favoritedDevicesJsonArray.put(JSONObject())

        appSettings.ignoredDevices.forEach {
            val tempJsonObject = JSONObject()

            tempJsonObject.putOpt("id", it.id)
            tempJsonObject.putOpt("name", it.name)
            ignoredDevicesJsonArray.put(tempJsonObject)
        }

        //Have to put a null object at the end or it doesn't work, kotlin's fault
        ignoredDevicesJsonArray.put(JSONObject())

        jsonObject.putOpt("favorited_devices", favoritedDevicesJsonArray)
        jsonObject.putOpt("ignored_devices", ignoredDevicesJsonArray)


        val sharedPref = (context as AppCompatActivity).getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)
        println("Writing ${jsonObject}")
        sharedPref.edit().putString("app_settings", jsonObject.toString(4)).apply()

        this.appSettings.value = appSettings

        println("Saved app settings")
    }
}

class AppSettings(
    var dataTimeout: Int? = null, //Data timeout in seconds
    val favoriteDevices: MutableList<SavedDevice> = mutableListOf(),
    val ignoredDevices: MutableList<SavedDevice> = mutableListOf(),

    var logAdvertisementData: Boolean = false,
    var logConnectionEvents: Boolean = false,
    var logDisconnectionEvents: Boolean = false,
    var logReadWriteEvents: Boolean = false,
    var logNotificationIndicationEvents: Boolean = false,
    var logGattDiscoveryEvents: Boolean = false,
    var logRssiUpdates: Boolean = false

)

class AppSettingsFromJson(json: String) : JSONObject(json) {
    val dataTimeout: Int? = if(this.optInt("data_timeout") == 0) null else this.optInt("data_timeout")

    //Logging values
    val logAdvertisementData: Boolean = this.optBoolean(DataKeys.logAdvertisementDataKey, false)
    val logConnectionEvents: Boolean = this.optBoolean(DataKeys.logConnectionEventsKey, false)
    val logDisconnectionEvents: Boolean = this.optBoolean(DataKeys.logDisconnectionEventsKey, false)
    val logReadWriteEvents: Boolean = this.optBoolean(DataKeys.logReadWriteEventsKey, false)
    val logNotificationIndicationEvents: Boolean = this.optBoolean(DataKeys.logNotificationIndicationEventsKey, false)
    val logGattDiscoveryEvents: Boolean = this.optBoolean(DataKeys.logGattDiscoveryEventsKey, false)
    val logRssiUpdates: Boolean = this.optBoolean(DataKeys.logRssiUpdatesKey, false)

    val favoritedDevices = this.optJSONArray("favorited_devices")?.let {
        0.until(it.length()-1).map { i ->
            it.optJSONObject(i)
        }
    }?.map {it ->
        SavedDevice(it)
    }

    val ignoredDevices = this.optJSONArray("ignored_devices")?.let {
        0.until(it.length()-1).map { i ->
            it.optJSONObject(i)
        }
    }?.map {it ->
        SavedDevice(it)
    }
}

class SavedDevice(macAddress: String?, name: String?){

    constructor(jsonObject: JSONObject) : this(jsonObject.optString("id"), jsonObject.optString("name"))

    val id: String? = macAddress
    val name: String? = name
}

//class SavedDevice(json: String) : JSONObject(json) {
//
//    val id: String? = this.optString("id")
//    val name: String? = this.optString("name")
//}